export function hashCode(str: string) {
  let hash = 0;
  if (!str) return hash;

  for (let i = 0; i < str.length; i++) {
    hash = (hash * 31 + str.charCodeAt(i)) | 0;
  }
  return Math.abs(hash);
}