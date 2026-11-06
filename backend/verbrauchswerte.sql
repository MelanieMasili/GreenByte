
-- GreenByte: Verbrauchswerte für fünf Kategorien
-- Vorläufige Modellwerte für das IU-Projekt
-- Spaltenreihenfolge ausdrücklich festgelegt.
-- Für eine leere, bereits angelegte Tabelle VERBRAUCHSWERT.

INSERT INTO VERBRAUCHSWERT
(KATEGORIE, LEISTUNG_WATT, QUELLE, MODELLANNAHME, NUTZUNGSSZENARIO)
VALUES
(
  'Laptop',
  12.5,
  'Notebookcheck – Lenovo ThinkPad T14, Leistungsmessungen',
  'Vorläufiger Modellwert: 12,5 Watt; angenommene Nutzungsverteilung',
  '80 % Leerlauf, 20 % Last'
),
(
  'Desktop-PC',
  67.0,
  'Notebookcheck – Desktop versus Notebook (2015)',
  'Vorläufiger Modellwert: 67 Watt; Monitor nicht berücksichtigt',
  '80 % Leerlauf, 20 % höhere Auslastung'
),
(
  'Konsole',
  154.0,
  'Microsoft Game Dev – Gaming Sustainability Lab Platform Baselines',
  'Referenzwert 154 Watt; ohne Fernseher und Standby',
  'Xbox Series X – aktives Gameplay'
),
(
  'Streaming',
  77.0,
  'IEA – The carbon footprint of streaming video (2020)',
  '77 Watt Äquivalentleistung; IEA-Modellwert für 2019 inklusive Endgerät, Netzwerk und Rechenzentrum',
  'Eine Stunde Videostreaming'
),
(
  'Smartphone',
  3.65,
  'Vorläufige Literaturannahme: 4 kWh/Jahr',
  '4000 Wh / (365 x 3 h) = ca. 3,65 W Äquivalentleistung; ohne Herstellung und Netzwerkinfrastruktur',
  '3 angenommene aktive Nutzungsstunden pro Tag'
);
