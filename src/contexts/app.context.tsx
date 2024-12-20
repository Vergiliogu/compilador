import { createContext, PropsWithChildren, ReactNode, useCallback, useEffect, useRef, useState } from "react";

interface AppContextType {
  terminalMessage?: ReactNode;
  statusFile?: string;
  terminalHeight: number;
  setTerminalHeight: (height: number) => void;
  editorText: string;
  handleEditContent: (text: string) => void;
  actions: {
    handleNew: () => void;
    handleOpenFile: () => void;
    handleSaveFile: () => void;
    handleCompile: () => void;
    handleShowTeam: () => void;
    handleCopyToClipboard: () => void;
    handlePasteFromClipboard: () => void;
    handleCutToClipboard: () => void;
  }
  editorRef: React.MutableRefObject<HTMLTextAreaElement | null>;
  errorLine: number | null;
  errorLineText: string;
}

interface LoadedFileMetaData {
  filePath: string;
  isNewFile: boolean;
}

export const AppContext = createContext<AppContextType | undefined>(undefined);

export const AppContextProvider = ({ children }: PropsWithChildren) => {
  const [terminalHeight, setTerminalHeight] = useState(100);
  const [terminalMessage, setTerminalMessage] = useState<ReactNode>('');
  const [statusFile, setStatusFile] = useState<string | undefined>(undefined);
  const [editorText, setEditorText] = useState<string>('');
  const [errorLine, setErrorLine] = useState<number | null>(null);
  const [errorLineText, setErrorLineText] = useState<string>('');
  const editorRef = useRef<HTMLTextAreaElement | null>(null);
  const compileTimerRef = useRef<NodeJS.Timeout>();
  const [loadedFileMetaData, setLoadedFileMetaData] = useState<LoadedFileMetaData>({
    filePath: '',
    isNewFile: true,
  });

  const handleNew = useCallback(() => {
    setTerminalMessage(undefined);
    setStatusFile(undefined);
    setEditorText('');
    setLoadedFileMetaData({ filePath: '', isNewFile: true });
  }, [])

  const handleOpenFile = useCallback(async () => {
    const filePath = await window.electron.openFileDialog();

    if (filePath?.length) {
      const fileContent = await window.electron.readFile(filePath[0]);
      setEditorText(fileContent || '');
      setStatusFile(filePath[0]);
      setTerminalMessage('');
      setLoadedFileMetaData({ filePath: filePath[0], isNewFile: false });
    }
  }, [])

  const handleSaveFile = useCallback(async () => {
    if (loadedFileMetaData.isNewFile) {
      const { success, filePath } = await window.electron.openSaveFileDialog(editorText);
      if (!success) return;

      setStatusFile(filePath);
      setTerminalMessage('');
      setLoadedFileMetaData({ filePath: filePath, isNewFile: false });
    } else {
      const { success } = await window.electron.writeFile(loadedFileMetaData.filePath, editorText);
      if (!success)
        setTerminalMessage('Não foi possível salvar o arquivo.');
      else
        setTerminalMessage('');
    }
  }, [editorText, loadedFileMetaData])

  const handleCompile = useCallback(async () => {
    if (loadedFileMetaData.isNewFile) {
      const { success } = await window.electron.writeCompilerFile(editorText);
      if (!success) return setTerminalMessage('Não foi possível compilar o código. 1');
    }

    const compiler = await window.electron.runCompiler(loadedFileMetaData.isNewFile ? undefined : loadedFileMetaData.filePath);
    if (!compiler.success) return setTerminalMessage('Não foi possível compilar o código. 2');

    setTerminalMessage(compiler.output);
  }, [editorText, loadedFileMetaData])

  const handleCompileInLine = useCallback(async (text: string) => {
    // const { success } = await window.electron.writeCompilerFile(text);
    // if (!success) return;
    
    // const compiler = await window.electron.runCompiler();

    // const errorIndex = compiler.output.indexOf('Erro na linha ')
    // if (errorIndex !== -1) {
    //   const postLine = compiler.output.indexOf(' -')
    //   const line = parseInt(compiler.output.slice(errorIndex + 14, postLine));
    //   setErrorLine(line);
    //   setErrorLineText(compiler.output.slice(postLine + 3));
    // } else {
    //   setErrorLine(null);
    //   setErrorLineText('');
    // }
  }, [])

  const handleShowTeam = useCallback(() => {
    setTerminalMessage(
      <>
        - Gustavo Vergilio Poleza <br />
        - Lemuel Kauê Manske de Liz <br />
        - João Henrique Stolf Galeazzi
      </>
    )
  }, [])

  const handleCopyToClipboard = useCallback(async () => {
    if (!editorRef.current) return;

    const start = editorRef.current.selectionStart;
    const end = editorRef.current.selectionEnd;
    const selectedText = editorRef.current.value.substring(start, end);
    await navigator.clipboard.writeText(selectedText);
  }, [])

  const handleCutToClipboard = useCallback(async () => {
    if (!editorRef.current) return;

    const start = editorRef.current.selectionStart;
    const end = editorRef.current.selectionEnd;
    const selectedText = editorRef.current.value.substring(start, end);

    editorRef.current.value = editorRef.current.value.substring(0, start) + editorRef.current.value.substring(end);
    editorRef.current.selectionStart = start;
    editorRef.current.selectionEnd = start;
    await navigator.clipboard.writeText(selectedText);
    setEditorText(editorRef.current.value);
  }, [])

  const handlePasteFromClipboard = useCallback(async () => {
    const text = await navigator.clipboard.readText()
    if (!editorRef.current) return;

    const start = editorRef.current.selectionStart;
    const end = editorRef.current.selectionEnd;
    editorRef.current.value = editorRef.current.value.substring(0, start) + text + editorRef.current.value.substring(end);
    editorRef.current.selectionStart = start + text.length;
    editorRef.current.selectionEnd = start + text.length;
    setEditorText(editorRef.current.value);
  }, [])

  useEffect(() => {
    const shortcuts = [
      { ctrl: true, key: 'N', action: handleNew },
      { ctrl: true, key: 'O', action: handleOpenFile },
      { ctrl: true, key: 'S', action: handleSaveFile },
      { ctrl: false, key: 'F7', action: handleCompile },
      { ctrl: false, key: 'F1', action: handleShowTeam },
    ]

    const handleKeyDown = (e: KeyboardEvent) => {
      const key = e.key.toUpperCase();
      const ctrl = e.ctrlKey || e.metaKey;
      const shortcut = shortcuts.find(s => s.key === key && s.ctrl === ctrl);

      if (shortcut) {
        e.preventDefault();
        shortcut.action();
      }
    }

    window.addEventListener('keydown', handleKeyDown);
    return () => {
      window.removeEventListener('keydown', handleKeyDown);
    }
  }, [
    handleNew,
    handleOpenFile,
    handleSaveFile,
    handleCompile,
    handleShowTeam,
    handleCopyToClipboard,
    handlePasteFromClipboard,
    handleCutToClipboard,
  ])

  const handleEditContent = (text: string) => {
    setEditorText(text);

    clearTimeout(compileTimerRef.current); 1

    compileTimerRef.current = setTimeout(() => {
      handleCompileInLine(text);
    }, 250);
  }

  return (
    <AppContext.Provider value={{
      statusFile,
      terminalMessage,
      terminalHeight,
      setTerminalHeight,
      editorText,
      handleEditContent,
      actions: {
        handleNew,
        handleOpenFile,
        handleSaveFile,
        handleCompile,
        handleShowTeam,
        handleCopyToClipboard,
        handlePasteFromClipboard,
        handleCutToClipboard,
      },
      editorRef,
      errorLine,
      errorLineText,
    }}>
      {children}
    </AppContext.Provider>
  );
}
