
<script setup>
import { onMounted } from 'vue'
import { ref, computed } from 'vue'
import { istEingabeGueltig } from './eingabePruefung.js'
import greenbyteLogo from './assets/greenbyte-logo.png'

// Unsere auswählbaren Geräte und Nutzungsszenarien
const geraete = [
  'Laptop',
  'Desktop-PC',
  'Konsole',
  'Streaming',
  'Smartphone',
]

// Speichert die aktuelle Auswahl
const ausgewaehltesGeraet = ref('')
const stundenProTag = ref('')
const berechneteAuswahl = ref(null)
const fehlermeldung = ref('')


// Erst wenn beides ausgewählt wurde,
// kann später eine Berechnung stattfinden.
const kannBerechnen = computed(() => {
  return istEingabeGueltig(
    ausgewaehltesGeraet.value,
    stundenProTag.value
  )
})


// Bei Gerätewechsel setzen wir die Stunden zurück.
function geraetAuswaehlen(geraet) {
  ausgewaehltesGeraet.value = geraet
  stundenProTag.value = ''
}


async function verbrauchBerechnen() {
  if (!kannBerechnen.value) {
    return
  }
    fehlermeldung.value = ''
    berechneteAuswahl.value = null

  try {
    const antwort = await fetch('http://localhost:8080/api/berechnung', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        geraet: ausgewaehltesGeraet.value,
        stunden: Number(stundenProTag.value),
      }),
    })

    if (!antwort.ok) {
      throw new Error(`HTTP-Fehler: ${antwort.status}`)
    }

    const ergebnis = await antwort.json()

    berechneteAuswahl.value = ergebnis

    console.log('Berechnung von Java:', ergebnis)
  
    } catch (fehler) {
      console.error('Berechnung fehlgeschlagen:', fehler)

      fehlermeldung.value =
        'Für diese Kategorie sind noch keine Verbrauchsdaten vorhanden.'
    }

}

  
onMounted(async () => {
  console.log('GreenByte: onMounted wurde gestartet!')
  try {
    const antwort = await fetch('http://localhost:8080/api/status')

    if (!antwort.ok) {
      throw new Error(`HTTP-Fehler: ${antwort.status}`)
    }

    const nachricht = await antwort.text()
    console.log('Antwort vom Java-Backend:', nachricht)
  } catch (fehler) {
    console.error('Backend-Verbindung fehlgeschlagen:', fehler)
  }
})


</script>

<template>
  <div class="greenbyte">
    
    <header class="kopfbereich">
      <img
        :src="greenbyteLogo"
        alt="GreenByte – Natur und digitale Technologie"
        class="greenbyte-logo"
      />

      <div class="kopftext">
        <h1>GreenByte</h1>
        <p>Wie nachhaltig ist deine digitale Nutzung?</p>
      </div>
    </header>


    <main class="hauptbereich">
      <section class="auswahlbereich">
        <h2>Wähle deine Nutzung</h2>
        <p class="beschreibung">
          Wähle ein Gerät oder eine digitale Aktivität aus.
        </p>

        
<div class="geraeteliste">
  <div
    v-for="geraet in geraete"
    :key="geraet"
    class="geraetegruppe"
  >
    <label class="geraetezeile">
      <input
        type="radio"
        name="geraet"
        :value="geraet"
        :checked="ausgewaehltesGeraet === geraet"
        @change="geraetAuswaehlen(geraet)"
      />

      <span class="geraetename">{{ geraet }}</span>
    </label>

    <div
      v-if="ausgewaehltesGeraet === geraet"
      class="stundenbereich"
    >
      <label :for="'stunden-' + geraet">
        Stunden pro Tag
      </label>

      <select
        :id="'stunden-' + geraet"
        v-model="stundenProTag"
      >
        <option value="" disabled>
          Bitte auswählen
        </option>

        <option
          v-for="stunde in 24"
          :key="stunde"
          :value="stunde"
        >
          {{ stunde }} {{ stunde === 1 ? 'Stunde' : 'Stunden' }}
        </option>
      </select>
    </div>
  </div>
</div>


        <button
          class="berechnen-button"
          :disabled="!kannBerechnen"
          @click="verbrauchBerechnen"
        >
          Verbrauch berechnen
        </button>
      </section>

      <section class="ergebnisbereich">
        <h2>Dein geschätzter Ressourcenverbrauch</h2>

        <p v-if="fehlermeldung" class="fehlermeldung" role="alert">
          {{ fehlermeldung }}
        </p>


        
        <p v-if="!berechneteAuswahl" class="hinweis">
          Wähle links ein Gerät und deine tägliche Nutzungsdauer aus.
        </p>

        <div v-else class="auswahl-ergebnis">
          <p>Deine ausgewählte Nutzung:</p>

          <strong>{{ berechneteAuswahl.geraet }}</strong>

          <span>
            {{ berechneteAuswahl.stunden }}
            {{ berechneteAuswahl.stunden === 1 ? 'Stunde' : 'Stunden' }}
            pro Tag
          </span>
        </div>


        <div class="ergebniskarten">
          <article class="ergebniskarte">
            <h3>💧 Wasser</h3>
            <p class="wert">
              {{ berechneteAuswahl?.wasserLiter != null
                ? berechneteAuswahl.wasserLiter.toLocaleString('de-DE', {
                    minimumFractionDigits: 3,
                    maximumFractionDigits: 5
                  }) + ' Liter'
                : '– Liter' }}
            </p>
            <div class="wasser-visualisierung">

            <div
              class="wasserbehaelter"
              role="img"
              :aria-label="berechneteAuswahl
                ? 'Füllstand: ' +
                  Math.min(berechneteAuswahl.wasserLiter * 100, 100).toFixed(1) +
                  ' Prozent einer 1-Liter-Flasche'
                : 'Noch kein Wasserverbrauch berechnet'"
            >
              <div
                class="wasserfuellung"
                :style="{
                  height: berechneteAuswahl
                    ? Math.min(berechneteAuswahl.wasserLiter * 100, 100) + '%'
                    : '0%'
                }"
              ></div>
            </div>

            <p v-if="berechneteAuswahl" class="wasser-vergleich">
              {{ (berechneteAuswahl.wasserLiter * 1000).toLocaleString('de-DE', {
                maximumFractionDigits: 1
              }) }} ml

              <span>
                von 1 Liter
              </span>
            </p>
          </div>
            <p>Geschätzter Wasserverbrauch bei der Stromerzeugung pro Tag.</p>
          </article>


          <article class="ergebniskarte">
            <h3>⚡ Energie</h3>
            <p class="wert">
              {{ berechneteAuswahl?.energieKwh != null
                ? berechneteAuswahl.energieKwh.toLocaleString('de-DE', {
                    minimumFractionDigits: 3,
                    maximumFractionDigits: 5
                  }) + ' kWh'
                : '– kWh' }}
            </p>           
            <div class="energie-visualisierung">
              <div
                class="energie-balken"
                role="progressbar"
                :aria-valuenow="Math.min(
                  berechneteAuswahl?.energieKwh ?? 0,
                  1
                )"
                aria-valuemin="0"
                aria-valuemax="1"
                aria-label="Energieverbrauch im Vergleich zu einer Kilowattstunde"
              >
                <div
                  class="energie-fuellung"
                  :style="{
                    width: Math.min(
                      (berechneteAuswahl?.energieKwh ?? 0) * 100,
                      100
                    ) + '%'
                  }"
                ></div>
              </div>

              <p v-if="berechneteAuswahl" class="energie-vergleich">
                {{ (berechneteAuswahl.energieKwh * 100).toLocaleString('de-DE', {
                  maximumFractionDigits: 2
                }) }} % von 1 kWh
              </p>
            </div>
            <p>Geschätzter Energieverbrauch pro Tag.</p>
          </article>

          
          <article class="ergebniskarte">
            <h3>🌳 CO₂e</h3>
            <p class="wert">
              {{ berechneteAuswahl?.co2kg != null
                ? berechneteAuswahl.co2kg.toLocaleString('de-DE', {
                    minimumFractionDigits: 3,
                    maximumFractionDigits: 5
                  }) + ' kg CO₂e'
                : '– kg CO₂e' }}
            </p>
            <div class="co2-visualisierung">
              <div class="co2-baum" aria-hidden="true">
                <div class="baumkrone"></div>
                <div class="baumstamm"></div>
              </div>

              <div v-if="berechneteAuswahl" class="co2-vergleich">
                <strong>
                  {{ (berechneteAuswahl.co2kg * 1000).toLocaleString('de-DE', {
                    maximumFractionDigits: 2
                  }) }} g CO₂e
                </strong>
              </div>
            </div>
            <p>Geschätzte CO₂-Emissionen pro Tag.</p>
          </article>

        </div>
      </section>
    </main>
  </div>
</template>



<style scoped>
.greenbyte {
  min-height: 100vh;
  background-color: #71856b;
  color: #fffaf0;
  padding: 40px;
  font-family: Arial, sans-serif;
}

.kopfbereich {
  display: flex;
  align-items: center;
  gap: 30px;
  margin-bottom: 70px;
}

.greenbyte-logo {
  width: 180px;
  height: auto;
  object-fit: contain;
  border-radius: 12px;
}

.kopftext h1 {
  font-size: 36px;
  margin-bottom: 8px;
}

.kopftext p {
  margin-top: 0;
}

@media (max-width: 600px) {
  .kopfbereich {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .greenbyte-logo {
    width: 150px;
  }
}


.hauptbereich {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 70px;
  max-width: 1400px;
  margin: 0 auto;
}

h2 {
  font-size: 25px;
  margin-bottom: 20px;
}

.beschreibung,
.hinweis {
  line-height: 1.6;
  opacity: 0.9;
}

.geraeteliste {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 30px;
}

.geraetezeile {
  display: flex;
  align-items: center;
  gap: 15px;
  min-height: 45px;
  cursor: pointer;
}

.geraetegruppe {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.geraetezeile {
  display: flex;
  align-items: center;
  gap: 15px;
  min-height: 45px;
  cursor: pointer;
}

.geraetezeile input[type='radio'] {
  accent-color: #344e41;
  width: 20px;
  height: 20px;
  flex-shrink: 0;
}

.geraetename {
  font-size: 18px;
}

.stundenbereich {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-left: 35px;
  max-width: 220px;
}

.stundenbereich label {
  font-size: 14px;
  color: #fffaf0;
}

.stundenbereich select {
  width: 100%;
  padding: 12px;
  border: 1px solid #344e41;
  border-radius: 8px;
  background-color: #fffaf0;
  color: #263c30;
  font-size: 15px;
}

.berechnen-button {
  margin-top: 35px;
  padding: 15px 30px;
  background-color: #344e41;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
}

.berechnen-button:hover:not(:disabled) {
  background-color: #263c30;
}

.berechnen-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.ergebniskarten {
  display: flex;
  flex-direction: column;
  gap: 18px;
  margin-top: 30px;
}

.ergebniskarte {
  background-color: rgba(255, 250, 240, 0.12);
  border: 1px solid rgba(255, 250, 240, 0.25);
  border-radius: 12px;
  padding: 18px 22px;
}

.ergebniskarte h3 {
  margin-top: 0;
}

.ergebniskarte .wert {
  font-size: 25px;
  font-weight: bold;
}

.ergebniskarte p:last-child {
  font-size: 14px;
  opacity: 0.85;
}

@media (max-width: 800px) {
  .greenbyte {
    padding: 24px;
  }

  .hauptbereich {
    grid-template-columns: 1fr;
    gap: 35px;
  }

  .kopfbereich {
    margin-bottom: 40px;
  }
}

.auswahl-ergebnis {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  margin-top: 20px;
  background-color: rgba(255, 250, 240, 0.12);
  border: 1px solid rgba(255, 250, 240, 0.25);
  border-radius: 10px;
}

.auswahl-ergebnis p {
  width: 100%;
  margin: 0;
  opacity: 0.9;
}

.auswahl-ergebnis strong {
  font-size: 20px;
}

.auswahl-ergebnis span {
  font-size: 16px;
}

.wasser-visualisierung {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  margin: 20px 0;
}


.wasserbehaelter {
  position: relative;
  width: 85px;
  height: 140px;
  border: 3px solid #91c7cf;
  border-radius: 12px 12px 18px 18px;
  background-color: rgba(185, 225, 235, 0.18);
  overflow: hidden;
  box-shadow: inset 0 0 12px rgba(140, 210, 230, 0.15);
}

.wasserfuellung {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background: linear-gradient(
    to top,
    #238bb5,
    #70d5ee
  );
  transition: height 0.6s ease;
}


.wasser-vergleich {
  text-align: center;
  font-weight: bold;
}

.wasser-vergleich span {
  display: block;
  font-size: 0.85rem;
  font-weight: normal;
}


.energie-visualisierung {
  margin: 24px 0;
}

.energie-balken {
  width: 100%;
  height: 24px;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, 0.15);
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.25);
}

.energie-fuellung {
  height: 100%;
  border-radius: 12px;
  background: linear-gradient(
    90deg,
    #e9ad43,
    #f8d77a
  );
  transition: width 0.6s ease;
}

.energie-vergleich {
  text-align: center;
  margin-top: 12px;
  font-size: 0.9rem;
}

.co2-visualisierung {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  margin: 24px 0;
}


.co2-baum {
  position: relative;
  width: 130px;
  height: 145px;
}

.baumstamm {
  position: absolute;
  bottom: 0;
  left: 58px;
  width: 15px;
  height: 82px;
  border-radius: 5px;
  background: linear-gradient(
    90deg,
    #65452f,
    #a57a50,
    #65452f
  );
  transform: rotate(2deg);
}

.baumkrone {
  position: absolute;
  top: 8px;
  left: 20px;
  width: 90px;
  height: 100px;
  border-radius: 48% 52% 44% 56%;
  background:
    radial-gradient(circle at 25% 35%, #a3c78b 0 18%, transparent 19%),
    radial-gradient(circle at 70% 25%, #82b573 0 25%, transparent 26%),
    radial-gradient(circle at 35% 75%, #568c55 0 28%, transparent 29%),
    radial-gradient(circle at 75% 70%, #386a43 0 30%, transparent 31%),
    #649b60;
  box-shadow:
    -18px 10px 0 -5px #6fa569,
    18px 8px 0 -5px #497b4e,
    0 -12px 0 -5px #83b879;
  z-index: 2;
}


.co2-vergleich {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
  text-align: center;
}

.co2-vergleich strong {
  font-size: 1.2rem;
}

.co2-vergleich span {
  font-size: 0.85rem;
  opacity: 0.85;
}


</style>
