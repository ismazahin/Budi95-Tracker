# BUDI MADANI Calculator 🇲🇾⛽

A Smart Petrol Cost Calculator for Malaysia with BUDI MADANI RON95 fuel subsidy calculation.

---

## 📱 Features

- **Home** — Complete information about BUDI MADANI RON95 subsidy programme
- **Calculator** — Accurate petrol cost calculator with step-by-step BUDI MADANI rebate breakdown
- **About** — Author information, app details, and GitHub link

---

## 🧮 Calculation Logic

| Step | Formula |
|------|---------|
| Step 1 | Total Petrol Cost = Fuel Usage × Petrol Price per Litre |
| Step 2 | BUDI Rebate = Fuel Usage × RM 1.99 *(RON95 eligible users only)* |
| Step 3 | Final Payable = Total Cost − BUDI Rebate |

### Sample Calculation
- Petrol type: RON95 @ RM 4.27/L
- Fuel usage: 40 litres
- BUDI MADANI eligible: YES

| | |
|--|--|
| Total Cost | 40 × RM 4.27 = **RM 170.80** |
| BUDI Rebate | 40 × RM 1.99 = **RM 79.60** |
| You Pay | RM 170.80 − RM 79.60 = **RM 91.20** |

---

## 📋 Petrol Prices (2025)

| Type | Price | BUDI MADANI |
|------|-------|-------------|
| RON 95 | RM 4.27/L | ✅ RM 1.99/L subsidy |
| RON 97 | RM 5.35/L | ❌ Not eligible |
| Diesel | RM 3.35/L | ❌ Not eligible |

---

## 🛠️ Setup Instructions

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/budi-madani-app.git
   ```
2. Open in **Android Studio Hedgehog** or newer
3. Let Gradle sync
4. Update `AboutFragment.java` with your personal details:
   ```java
   private static final String AUTHOR_NAME = "Your Full Name";
   private static final String MATRIC_NO   = "Your Matric Number";
   private static final String COURSE      = "Your Course Name";
   private static final String GITHUB_URL  = "https://github.com/yourrepo";
   ```
5. Run on an emulator or physical device (API 24+)

---

## 📐 Requirements

- Android Studio Hedgehog (2023.1.1) or newer
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
