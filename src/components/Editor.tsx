import { useState } from "react";
import { twMerge } from "tailwind-merge";
import { useAppContext } from "../contexts/useAppContext";

const editorRowHeight = 1.5; // rem = 24px
const toolsBarHeight = 70; // px
const statusBarHeight = 25; // px

export const Editor = () => {
  const [editorRows, setEditorRows] = useState(1);
  const [editorLongestColumn, setEditorLongestColumn] = useState(1);

  const { terminalHeight } = useAppContext()

  const handleEditorContent = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newValue = e.target.value;

    const lines = newValue.split('\n')

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
  }

  return (
    <div className="w-full flex-grow overflow-y-hidden">
      <div
        style={{ height: `calc(100vh - ${terminalHeight}px - ${toolsBarHeight}px - ${statusBarHeight}px)` }}
      >
        <div className="w-full h-full flex flex-row overflow-scroll">
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
            className={twMerge(
              'flex-grow bg-transparent pt-4 pl-2 text-lg resize-none outline-none leading-6 whitespace-nowrap',
              'min-h-full overflow-y-hidden overflow-x-hidden font-mono',
            )}
            style={{
              height: `calc(${editorRows * editorRowHeight}rem + 25px)`,
              minWidth: `${editorLongestColumn}ch`,
            }}
            onChange={handleEditorContent}
            defaultValue={''}
          />
        </div>
      </div>
    </div>
  );
}
