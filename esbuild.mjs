import path from "path"

import esbuild from "esbuild"
import browserslist from "browserslist"

import { resolveToEsbuildTarget } from "esbuild-plugin-browserslist"

import { NodeModulesPolyfillPlugin } from '@esbuild-plugins/node-modules-polyfill'
import { NodeGlobalsPolyfillPlugin } from '@esbuild-plugins/node-globals-polyfill'

const PROVIDER_CONTENTS = `
import nodeCreateHash from "create-hash"
import {
  createCipheriv as nodeCreateCipheriv,
  createDecipheriv as nodeCreateDecipheriv,
} from "browserify-cipher"

export const createHash = (algo) => {
  const hash = nodeCreateHash(algo)
  if (!hash) throw new Error("Cannot create hash for algorithm '" + algo + "'")
  return hash
}

export const createCipheriv = (algo, key, iv) => {
  const cipher = nodeCreateCipheriv(algo, key, iv)
  if (!cipher) throw new Error("Cannot create cipher for algorithm '" + algo + "'")
  return cipher
}

export const createDecipheriv = (algo, key, iv) => {
  const decipher = nodeCreateDecipheriv(algo, key, iv)
  if (!decipher) throw new Error("Cannot create decipher for algorithm '" + algo + "'")
  return decipher
}

export default {
  createHash,
  createCipheriv,
  createDecipheriv,
}
`

const INDEX_CONTENTS = `
// make Buffer polyfill available
globalThis.Buffer = Buffer;

// re-export TeaVM bundle for CryptoBrief
import { init, setLocale, execute } from "./target/teavm/cryptobrief-web.js"
export { init, setLocale, execute }
export default { init, setLocale, execute }

import { LOG_LEVEL } from "./target/teavm/cryptobrief-web.js"
export const LOG_LEVEL_RESULT = LOG_LEVEL.RESULT;
export const LOG_LEVEL_ERROR = LOG_LEVEL.ERROR;
export const LOG_LEVEL_WARNING = LOG_LEVEL.WARNING;
`

function inlineFilePlugin(contents, filename, extension = "js") {
  return {
    name: "inlineFilePlugin " + filename + "." + extension,
    setup(build) {
      const resolveRe = new RegExp(`^${filename}\.${extension}$`)
      const pathRe = new RegExp(`(?:.*/|^)${filename}(?:\.${extension})?$`)
        build.onResolve({ filter: resolveRe }, (args) => {
          // dummy path in the current root for inline file,
          // mark as inline for content insertion
          return {
            path: path.resolve(args.resolveDir, `./${filename}.${extension}`),
            pluginData: Object.assign(args.pluginData || {}, {
              isInline: true,
            }),
          }
        })
        build.onLoad({ filter: pathRe }, (args) => {
          // only load inline contents, if file is marked as such
          if (!args.pluginData?.isInline) return
          return { contents, loader: 'js' }
        })
    }
  }
}

console.log("Generate CommonJS dist/cryptobrief-web.js ...")

const target = resolveToEsbuildTarget(browserslist(), {
  printUnknownTargets: false,
});

await esbuild.build({
  entryPoints: ["index.mjs"],
  outfile: "dist/cryptobrief-web.js",
  bundle: true,
  minify: true,
  sourcemap: "linked",
  format: "esm",
  target: target,
  plugins: [
    // inline entrypoint
    inlineFilePlugin(INDEX_CONTENTS, "index", "mjs"),
    // crypto API wrapper
    inlineFilePlugin(PROVIDER_CONTENTS, "cryptoProvider", "mjs"),
    // polyfills for the crypto API
    NodeModulesPolyfillPlugin(),
    NodeGlobalsPolyfillPlugin({
      buffer: true,
      process: false,
    }),
  ]
})

console.log("⚡Done! ⚡")
