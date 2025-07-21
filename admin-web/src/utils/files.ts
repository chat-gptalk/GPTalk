export function downloadFile(value: string, filename: string) {
  const blob = new Blob([value], {type: 'text/plain;charset=utf-8'});
  const url = URL.createObjectURL(blob);

  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();

  document.body.removeChild(a);
  URL.revokeObjectURL(url);
}
