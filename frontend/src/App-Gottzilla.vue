
<script setup>
import { onMounted } from 'vue'
import { ref, computed } from 'vue'
import { istEingabeGueltig } from './eingabePruefung.js'
import greenbyteLogo from './assets/greenbyte-logo.png'

// auswählbare Geräte und Nutzungsszenarien
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


// Erst wenn beides ausgewählt wurde,
// kann später eine Berechnung stattfinden.
const kannBerechnen = computed(() => {
  return istEingabeGueltig(
    ausgewaehltesGeraet.value,
    stundenProTag.value
  )
})


// Bei Gerätewechsel setzen Stunden zurücksetzen.
function geraetAuswaehlen(geraet) {
  ausgewaehltesGeraet.value = geraet
  stundenProTag.value = ''
}


async function verbrauchBerechnen() {
  if (!kannBerechnen.value) {
    return
  }

  const anfrage = {
    geraet: ausgewaehltesGeraet.value,
    stunden: Number(stundenProTag.value),
  }

  try {
    const antwort = await fetch('http://localhost:8080/api/berechnung', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(anfrage),
    })

    if (!antwort.ok) {
      throw new Error(`HTTP-Fehler: ${antwort.status}`)
    }

    berechneteAuswahl.value = await antwort.json()

    console.log('Antwort vom Java-Backend:', berechneteAuswahl.value)
  } catch (fehler) {
    console.error('Berechnung fehlgeschlagen:', fehler)
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
            <p class="wert">– Liter</p>
            <p>Hier erscheint später unser Wasservergleich.</p>
          </article>

          <article class="ergebniskarte">
            <h3>⚡ Energie</h3>
            <p class="wert">– kWh</p>
            <p>Hier erscheint später unser Energievergleich.</p>
          </article>

          <article class="ergebniskarte">
            <h3>🌳 CO₂e</h3>
            <p class="wert">– kg CO₂e</p>
            <p>Hier erscheint später unser CO₂-Vergleich.</p>
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

</style>
