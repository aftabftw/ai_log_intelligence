import { useEffect, useState } from "react";
import axios from "axios";

export default function RootCauseTable({ onSelect }) {
  const [data, setData] = useState([]);

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/logs/root-causes")
      .then((res) => setData(res.data));
  }, []);

  return (
    <div className="bg-white p-4 rounded-xl shadow">
      <h2 className="text-xl font-semibold mb-3">Root Causes</h2>

      <table className="w-full">
        <thead>
          <tr className="border-b">
            <th className="text-left">Group</th>
            <th className="text-left">Frequency</th>
          </tr>
        </thead>

        <tbody>
          {data.map((item) => (
            <tr
              key={item.groupId}
              className="cursor-pointer hover:bg-gray-100 border-b"
              onClick={() => onSelect(item.groupId)}
            >
              <td>{item.groupId}</td>
              <td>{item.frequency}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
