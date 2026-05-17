# 🚗 Budi95 Tracker 🇲🇾⛽

A Smart Petrol Cost Calculator for Malaysia with live API integration and BUDI MADANI RON95 fuel subsidy calculation.

---

## 🚀 Live Demo & Download

- **📥 Download the App:** [Click here to download the latest APK](https://github.com/ismazahin/Budi95-Tracker/releases)
- **🌐 Run in Browser:** *(Optional: If you created an Appetize.io link earlier, paste it here so your lecturer can test it instantly!)*

---

## ✨ Features

- 🌐 **Live Market Data:** Integrates directly with the official `data.gov.my` API using `OkHttp3` to fetch real-time daily fuel prices.
- 🧮 **Smart Calculation Engine:** Accurate petrol cost calculator with step-by-step BUDI MADANI rebate breakdown. Input either Litres (L) or Total Amount (RM).
- 🎨 **Modern UI/UX:** Includes a custom Malaysia Madani splash screen (with Android 12+ API 31 seamless loading) and custom adaptive launcher icons.
- 📱 **App Navigation:**
  - **Home** — Complete information about the BUDI MADANI programme.
  - **Calculator** — The core transaction and savings engine.
  - **About** — Developer information and project details.

---

## 🧮 Calculation Logic

| Step | Formula |
|------|---------|
| Step 1 | Total Petrol Cost = Fuel Usage × Live Petrol Price per Litre |
| Step 2 | BUDI Rebate = Fuel Usage × RM 1.99 *(RON95 eligible users only)* |
| Step 3 | Final Payable = Total Cost − BUDI Rebate |

### Sample Calculation (Example Rates)
- Petrol type: RON95 @ RM 3.87/L *(Fetched live from API)*
- Fuel usage: 40 litres
- BUDI MADANI eligible: YES

| | |
|--|--|
| Total Cost | 40 × RM 3.87 = **RM 154.80** |
| BUDI Rebate | 40 × RM 1.99 = **RM 79.60** |
| You Pay | RM 154.80 − RM 79.60 = **RM 75.20** |

---

## 🛠️ Setup Instructions

1. Clone the repository:
   ```bash
   git clone [https://github.com/ismazahin/Budi95-Tracker.git](https://github.com/ismazahin/Budi95-Tracker.git)
---

## 📐 Requirements

- IDE: Android Studio Hedgehog (2023.1.1) or newer
- Min SDK: API 24 (Android 7.0)
- Target SDK: API 34 (Android 14)

---

## 🎨 Theme

- **Primary**: Blue `#0D47A1`
- **Accent**: Yellow `#FFD600`
- **Navigation**: Blue bottom nav with yellow active indicator

---

## 👨‍💻 Author

- **Name**: Your Name Here  
- **Matric No**: CS12345  
- **Course**: Mobile Technology (CSC482)

---

## 📄 License

© 2025 BUDI MADANI Calculator. All Rights Reserved.

---

*This app is developed as an individual assignment for Mobile Technology subject.*
