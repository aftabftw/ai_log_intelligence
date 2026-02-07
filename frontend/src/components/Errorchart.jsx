import { Line } from "react-chartjs-2";
import { Chart, LineElement, CategoryScale, LinearScale, PointElement } from "chart.js";

Chart.register(LineElement, CategoryScale, LinearScale, PointElement);

export default function ErrorChart({ logs }) {
  if (!logs || logs.length === 0) return null;

  const grouped = {};
  logs.forEach(l => {
    const minute = l.timestamp.substring(0,16);
    grouped[minute] = (grouped[minute] || 0) + 1;
  });

  const labels = Object.keys(grouped);
  const values = Object.values(grouped);

  return (
    <Line
      data={{
        labels,
        datasets: [{ label: "Errors", data: values }]
      }}
    />
  );
}
