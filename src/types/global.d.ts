export interface Token {
  id: string;
  lexeme: string;
  lineNumber: string;
}

export interface ElectronAPI {
  openFileDialog: () => Promise<string | null>;
  openSaveFileDialog: (content: string) => Promise<{ success: boolean, filePath: string, error?: any }>;
  readFile: (filePath: string) => Promise<string | null>;
  writeFile: (filePath: string, content: string) => Promise<{ success: boolean, filePath: string, error?: any }>;
  writeLexicalFile: (content: string) => Promise<{ success: boolean, filePath: string, error?: any }>;
  runLexical: () => Promise<{ success: boolean, output: string, error?: any }>;
}

declare global {
  interface Window {
    electron: ElectronAPI;
  }
}