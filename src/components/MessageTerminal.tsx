import { useRef } from "react";
import { twMerge } from "tailwind-merge";
import { useAppContext } from "../contexts/useAppContext";

export const MessageTerminal = () => {
  const terminalRef = useRef<HTMLDivElement | null>(null);

  const { setTerminalHeight, terminalHeight } = useAppContext()

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
      className={twMerge(
        'w-full min-h-[100px] max-h-[80%] overflow-hidden pb-2',
        'absolute bottom-[25px] left-0 right-0',
      )}
      style={{ height: terminalHeight }}
      ref={terminalRef}
    >
      <div
        onMouseDown={handleResize}
        className="w-full h-[8px] bg-gray-400 cursor-ns-resize"
      />

      <div className="w-full h-full overflow-scroll">
        <p className="w-full h-full bg-transparent p-2">
          Write Write Write Wri
        </p>
      </div>
    </div>
  );
}
