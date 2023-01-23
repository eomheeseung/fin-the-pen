import { Box, Stack } from '@mui/material';

function MarkerStack({ categoryForMarker }) {
  return (
    <Stack>
      <Stack direction="row" justifyContent="center" spacing={0.5} mt={0.2}>
        {categoryForMarker.slice(0, 3).map((s) => (
          <Box
            key={Math.random()}
            sx={{
              width: '5px', height: '5px', border: '1px solid', borderRadius: 3, borderColor: s.color,
            }}
          />
        ))}
      </Stack>
      <Stack direction="row" justifyContent="center" spacing={0.5} mt={0.2}>
        {categoryForMarker.slice(3, 7).map((s) => (
          <Box
            key={Math.random()}
            sx={{
              width: '5px', height: '5px', border: '1px solid', borderRadius: 3, borderColor: s.color,
            }}
          />
        ))}
      </Stack>
    </Stack>
  );
}

export default MarkerStack;
