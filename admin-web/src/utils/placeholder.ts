export type PlaceholderType = typeof Placeholder;

export const Placeholder = {
  type: 'placeholder',
} as const;

function isPlaceholder(item: any) {
  return item === Placeholder;
}

export function isNotPlaceholder<T>(item: PlaceholderType | T): item is T {
  return !isPlaceholder(item);
}