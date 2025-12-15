# Fix Webpack HMR Error

The error "Cannot access '__WEBPACK_DEFAULT_EXPORT__' before initialization" is a webpack hot module replacement issue.

## Solution:

1. **Stop the dev server** (Ctrl+C in the terminal)

2. **Clear webpack cache and node_modules cache:**
   ```bash
   cd frontend
   rm -rf node_modules/.cache
   # Or on Windows PowerShell:
   Remove-Item -Recurse -Force node_modules\.cache
   ```

3. **Restart the dev server:**
   ```bash
   npm start
   ```

## Alternative (if above doesn't work):

1. Stop the dev server
2. Delete `node_modules/.cache` folder
3. Restart: `npm start`

The issue occurs because webpack's hot module replacement gets confused when new packages are installed while the server is running.

