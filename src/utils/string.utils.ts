import { Token } from "../types/global";

export const addSpacesAtTheEnd = (text: string, targetLength: number): string => {
  return text + ' '.repeat(targetLength - text.length);
}

export const createLexicalTable = (tokens: Token[]): string => {
  let table = addSpacesAtTheEnd('linha', 10) + addSpacesAtTheEnd('classe', 20) + 'Line\n';

  tokens.forEach(token => {
    table += `${addSpacesAtTheEnd(token.lineNumber, 10)}${addSpacesAtTheEnd(token.id, 20)}${token.lexeme}\n`;
  });

  return table;
}

export const convertToHTMLSafe = (text: string): string => {
  return text.replace(/\n/g, '<br/>').replace(/\s/g, '&nbsp;');
}