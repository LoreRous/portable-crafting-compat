# Guida alla pubblicazione (per LoreRous)

Tutto il testo destinato agli utenti è in inglese, perché su Modrinth e CurseForge il pubblico è internazionale.

## File pronti

| File | A cosa serve |
|---|---|
| `publishing/icon-512.png` | Icona provvisoria: sostituiscila con la tua (e anche `src/main/resources/assets/pcccompat/icon.png`, 128×128, che va dentro al jar) |
| `publishing/DESCRIPTION.md` | Descrizione lunga, da incollare su Modrinth e CurseForge |
| `README.md` | Pagina principale del repository GitHub |
| `CONTRIBUTING.md` | Spiega agli altri come aggiungere compatibilità |
| `LICENSE` | Licenza MIT |
| `build/libs/portable-crafting-compat-1.0.0.jar` | Il file da caricare (NON il `-sources.jar`) |

## Descrizione breve (summary)

Inglese, per Modrinth e CurseForge (max ~250 caratteri):

> General compatibility add-on for Portable Crafting: use modded workstations straight from your inventory. Currently supports Uncraft Everything and Precise Enchanting.

Versione italiana, se ti serve:

> Add-on di compatibilità generale per Portable Crafting: usa i banchi da lavoro delle altre mod direttamente dall'inventario. Al momento supporta Uncraft Everything e Precise Enchanting.

## 1. GitHub

1. Su github.com crea un repository **pubblico** chiamato `portable-crafting-compat`, **vuoto** (senza README, licenza né .gitignore: ci sono già).
2. Il repository git locale è già inizializzato. Fai il primo commit e il push (da GitHub Desktop, oppure chiedimelo).
3. Facoltativo: crea una *Release* `v1.0.0` e allega il jar.

## 2. Modrinth (modrinth.com → Create a project)

- **Name:** Portable Crafting Compat, **URL:** `portable-crafting-compat`, **Type:** Mod
- **Summary:** la descrizione breve qui sopra
- **Description:** incolla `publishing/DESCRIPTION.md`
- **Icon:** `publishing/icon-512.png`
- **Categories:** Utility, Storage (o Equipment), **Environment:** Client *required*, Server *required*
- **License:** MIT, **Source:** `https://github.com/LoreRous/portable-crafting-compat`, **Issues:** `.../issues`
- **Versione:** carica il jar, numero `1.0.0`, Game version `1.21.1`, Loader `Fabric`, Release
- **Dipendenze:** Portable Crafting e Fabric API → *Required*; Uncraft Everything e Precise Enchanting → *Optional*

## 3. CurseForge (authors.curseforge.com → Create project)

Gli stessi dati di Modrinth: summary, descrizione (l'editor accetta Markdown), icona, licenza MIT e link al sorgente. Nel file: Game version 1.21.1, Modloader Fabric, e le stesse dipendenze (*Required* / *Optional*).

## Prima di pubblicare: prova questi casi

- [ ] Cobblemon e l'istanza test si avviano senza errori con la nuova versione
- [ ] Uncrafting Table e Arcane Disenchanter si aprono dall'inventario, come prima
- [ ] Il pulsante delle impostazioni dell'Uncrafting Table portatile non va in crash
- [ ] Il gioco si avvia con **solo** Uncraft Everything, e con **solo** Precise Enchanting
- [ ] Uscire e rientrare nel mondo con un menu aperto non lascia blocchi "fantasma" in cielo
