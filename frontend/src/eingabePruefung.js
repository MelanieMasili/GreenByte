
export function istEingabeGueltig(geraet, stunden) {
  return (
    typeof geraet === 'string' &&
    geraet.trim() !== '' &&
    Number.isInteger(Number(stunden)) &&
    Number(stunden) >= 1 &&
    Number(stunden) <= 24 &&
    stunden !== ''
  )
}
