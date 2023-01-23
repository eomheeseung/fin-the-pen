import { ResponsivePie } from '@nivo/pie';

function AnalysisGraph({ data, total }) {
  return (
    <ResponsivePie
      data={data}
      margin={{
        top: 0, right: 0, bottom: 0, left: 0,
      }}
      sortByValue
      colors={data.map((d) => d.color)}
      enableArcLinkLabels={false}
      arcLinkLabelsSkipAngle={10}
      arcLinkLabelsTextColor="#333333"
      arcLinkLabelsThickness={2}
      arcLinkLabelsColor={{ from: 'color' }}
      arcLabel={(e) => { const percent = (parseInt(e.value, 10) / total) * 100; return `${e.id} (${percent.toFixed(1)}%)`; }}
      arcLabelsSkipAngle={7}
      arcLabelsTextColor={{
        from: 'color',
        modifiers: [
          [
            'darker',
            '2',
          ],
        ],
      }}
      legends={[]}
    />
  );
}

export default AnalysisGraph;
