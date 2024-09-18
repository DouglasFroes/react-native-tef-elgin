import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-tef-elgin' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// eslint-disable-next-line prettier/prettier
const TefElgin = NativeModules.TefElgin ? NativeModules.TefElgin : new Proxy({}, { get() { throw new Error(LINKING_ERROR); }, });

export function onInitTef(): void {
  TefElgin.onInitTef();
}

type IConfigTef = {
  name: string;
  version: string;
  pinpad: string;
  doc: string;
};

export function configTef({ name, version, pinpad, doc }: IConfigTef): void {
  TefElgin.configTef(name, version, pinpad, doc);
}

export function payDeb(value: number): void {
  let valueToString = (value / 100).toFixed(2).toString();

  TefElgin.payDeb(valueToString);
}

export function payCred(
  value: number,
  type: string,
  installments: string
): void {
  let valueToString = (value / 100).toFixed(2).toString();

  TefElgin.payCred(valueToString, type, installments);
}

export function payPix(value: number): void {
  let valueToString = (value / 100).toFixed(2).toString();

  TefElgin.payPix(valueToString);
}

export function onCancel(): void {
  TefElgin.onCancel();
}
