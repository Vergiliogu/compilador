import { useRef, useState } from "react";

export const MessageTerminal = () => {
  const [terminalHeight, setTerminalHeight] = useState(100);
  const terminalRef = useRef<HTMLDivElement | null>(null);

  const handleResize = (e: React.MouseEvent<HTMLDivElement>) => {
    e.preventDefault();
    const initialY = e.clientY;
    const initialHeight = terminalRef.current?.offsetHeight || 0;

    const onMouseMove = (e: MouseEvent) => {
      const difference = initialHeight + initialY - e.clientY;
      const finalHeight = difference < 100 ? 100 : difference;

      setTerminalHeight(finalHeight);
    };

    const onMouseUp = () => {
      window.removeEventListener("mousemove", onMouseMove);
      window.removeEventListener("mouseup", onMouseUp);
    };

    window.addEventListener("mousemove", onMouseMove);
    window.addEventListener("mouseup", onMouseUp);
  }

  return (
    <div
      className="w-full flex-grow-0 min-h-[100px] bg-red-600 overflow-hidden pb-2"
      style={{ height: terminalHeight }}
      ref={terminalRef}
    >
      <div
        onMouseDown={handleResize}
        className="w-full h-[8px] bg-gray-400 cursor-ns-resize"
      />

      <div className="w-full h-full overflow-scroll">
        <p className="w-full h-full bg-transparent p-2 text-white">
          WriteWriteWriteWri
        </p>
      </div>
    </div>
  );
}
