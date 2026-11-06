
import { describe, it, expect } from 'vitest'
import { istEingabeGueltig } from './eingabePruefung.js'

describe('GreenByte – Eingabeprüfung', () => {
  it('akzeptiert ein Gerät mit gültigen Stunden', () => {
    expect(istEingabeGueltig('Laptop', 3)).toBe(true)
  })

  it('lehnt eine Eingabe ohne Gerät ab', () => {
    expect(istEingabeGueltig('', 3)).toBe(false)
  })

  it('lehnt eine Eingabe ohne Stunden ab', () => {
    expect(istEingabeGueltig('Laptop', '')).toBe(false)
  })

  it('lehnt 0 Stunden ab', () => {
    expect(istEingabeGueltig('Laptop', 0)).toBe(false)
  })

  it('lehnt mehr als 24 Stunden ab', () => {
    expect(istEingabeGueltig('Laptop', 25)).toBe(false)
  })

  it('lehnt Dezimalstunden ab', () => {
    expect(istEingabeGueltig('Laptop', 2.5)).toBe(false)
  })

  it('akzeptiert die obere Grenze von 24 Stunden', () => {
    expect(istEingabeGueltig('Streaming', 24)).toBe(true)
  })
})
