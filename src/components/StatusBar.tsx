import { useAppContext } from "../contexts/useAppContext";

export const StatusBar = () => {
  const { statusFile } = useAppContext()

  return (
    <div className="w-full h-[25px] bg-gray-300 flex flex-col justify-end border-t-2 border-gray-400">
      <p className="text-sm pl-2">
        {statusFile}
      </p>
    </div>
  );
}
