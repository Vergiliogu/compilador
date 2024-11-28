import { useEffect, useMemo, useState } from "react";
import { twMerge } from "tailwind-merge";
import { useAppContext } from "../contexts/useAppContext";

const editorRowHeight = 1.5; // rem = 24px
const toolsBarHeight = 70; // px
const statusBarHeight = 25; // px

export const Editor = () => {
  const [editorRows, setEditorRows] = useState(1);
  const [editorLongestColumn, setEditorLongestColumn] = useState(1);

  const { terminalHeight, editorText, handleEditContent, editorRef, errorLine, errorLineText } = useAppContext()

  const handleEditorContent = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newValue = e.target.value;
    handleEditContent(newValue);
  }

  useEffect(() => {
    const lines = editorText.split('\n')

    let longestAmountofChars = 0;
    for (let i = 0; i < lines.length; i++) {
      if (lines[i].length > longestAmountofChars)
        longestAmountofChars = lines[i].length;
    }

    // Get how many columns the editor has based on the longest line
    if (longestAmountofChars !== editorLongestColumn)
      setEditorLongestColumn(longestAmountofChars);

    // Get how many rows the editor has based on the number of line breaks
    if (lines.length !== editorRows)
      setEditorRows(lines.length);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [editorText])

  const errorRowLength = useMemo(() => {
    if (errorLine === null) return;

    const lines = editorText.split('\n')
    const lineText = lines[errorLine - 1] || '';

    return lineText.length;
  }, [editorText, errorLine])

  return (
    <div className="w-full flex-grow overflow-y-hidden">
      <div
        style={{ height: `calc(100vh - ${terminalHeight}px - ${toolsBarHeight}px - ${statusBarHeight}px)` }}
      >
        <div className="w-full h-full flex flex-row overflow-scroll relative">
          <div
            className="min-w-10 w-fit py-4 min-h-full bg-gray-300 border-r-2 border-gray-400"
            style={{ height: `calc(${editorRows * editorRowHeight}rem + 25px)` }}
          >
            {Array.from({ length: editorRows }).map((_, index) => (
              <div key={index} className="leading-6 text-end pr-2">
                {index + 1}
              </div>
            ))}
          </div>

          <textarea
            ref={editorRef}
            className={twMerge(
              'flex-grow bg-transparent pt-4 pl-2 text-lg resize-none outline-none leading-6 whitespace-nowrap',
              'min-h-full overflow-y-hidden overflow-x-hidden font-mono',
            )}
            style={{
              height: `calc(${editorRows * editorRowHeight}rem + 25px)`,
              minWidth: `${editorLongestColumn}ch`,
            }}
            onChange={handleEditorContent}
            value={editorText}
          />

          <div
            className={twMerge(
              "absolute left-10 bg-red-400/20",
              errorLine === null && 'hidden',
            )}
            style={{
              top: `calc(${(errorLine || 0) * editorRowHeight}rem - 7px)`,
              height: `${editorRowHeight}rem`,
              width: `calc(100% - 2.5rem)`,
            }}
          >
            <span style={{
              marginLeft: `calc(${errorRowLength}ch + 2.5rem)`,
            }}>
              {errorLineText}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
