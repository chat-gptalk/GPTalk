import {hashCode} from "@/utils/hash";

const Colors = ['#00BF6D', '#f56a00', '#7265e6', '#ffbf00', '#00a2ae'];

export function hashColor(value: string): string {
  return Colors[hashCode(value) % Colors.length];
}