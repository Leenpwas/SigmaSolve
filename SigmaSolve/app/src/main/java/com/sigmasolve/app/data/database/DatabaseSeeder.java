package com.sigmasolve.app.data.database;

import com.sigmasolve.app.data.entity.Formula;
import com.sigmasolve.app.data.entity.Problem;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseSeeder — provides initial seed data for formulas and problems.
 * Includes a comprehensive list of Electronics and Engineering formulas and step-by-step solvers.
 */
public class DatabaseSeeder {

    public static void seedAll(SigmaSolveDatabase db) {
        // We use a simple list approach. In a production app, we'd handle ID mapping more strictly.
        seedFormulasAndProblems(db);
    }

    private static void seedFormulasAndProblems(SigmaSolveDatabase db) {
        List<Formula> formulas = new ArrayList<>();
        List<Problem> problems = new ArrayList<>();

        // ── 1. SEMICONDUCTOR PHYSICS ────────────────────────────────────────
        formulas.add(new Formula("Year 2", "ECE", "Semiconductor Physics", "Carrier Concentration",
                "Intrinsic Carrier Concentration", "ni = sqrt(n * p)", "ni=intrinsic, n=electrons, p=holes", "Mass action law at equilibrium.", "", "", "", ""));
        problems.add(new Problem("Intrinsic Concentration Solver", "Find ni given electron and hole concentrations.", "Semiconductor Physics", 1,
                "[{\"symbol\":\"n\",\"name\":\"n-type concentration\",\"unit\":\"cm⁻³\"},{\"symbol\":\"p\",\"name\":\"p-type concentration\",\"unit\":\"cm⁻³\"}]",
                "ni", "[\"Apply Mass Action Law: ni² = n * p\",\"Calculate: ni = sqrt(n * p)\"]", "Easy"));

        formulas.add(new Formula("Year 2", "ECE", "Semiconductor Physics", "Transport",
                "Drift Current Density", "J = q(nμn + pμp)E", "q=charge, n,p=density, μ=mobility, E=field", "Current due to electric field.", "", "", "", ""));
        problems.add(new Problem("Drift Current Solver", "Calculate current density J.", "Semiconductor Physics", 2,
                "[{\"symbol\":\"n\",\"name\":\"n\",\"unit\":\"cm⁻³\"},{\"symbol\":\"μn\",\"name\":\"mobility\",\"unit\":\"\"},{\"symbol\":\"E\",\"name\":\"Field\",\"unit\":\"V/cm\"}]",
                "J", "[\"Use J = q * n * μ * E\",\"Assume q = 1.6e-19 C\"]", "Medium"));

        formulas.add(new Formula("Year 2", "ECE", "Semiconductor Physics", "Transport",
                "Einstein Relation", "D/μ = Vt", "D=diffusion, μ=mobility, Vt=thermal voltage", "Relation between D and μ.", "", "", "", ""));
        problems.add(new Problem("Einstein Relation Solver", "Find Diffusion constant D.", "Semiconductor Physics", 3,
                "[{\"symbol\":\"μ\",\"name\":\"Mobility\",\"unit\":\"cm²/V-s\"},{\"symbol\":\"Vt\",\"name\":\"Thermal Voltage\",\"unit\":\"mV\"}]",
                "D", "[\"D = μ * Vt\",\"Ensure Vt is in Volts\"]", "Easy"));

        // ── 2. DIODES ──────────────────────────────────────────────────────
        formulas.add(new Formula("Year 2", "ECE", "Electronics", "Diodes",
                "Diode Current Equation", "I = Is(e^(V/ηVt) - 1)", "Is=saturation current, V=bias, Vt=thermal", "Shockley equation.", "", "", "", ""));
        problems.add(new Problem("Diode Current Solver", "Find forward current I.", "Electronics", 4,
                "[{\"symbol\":\"Is\",\"name\":\"Saturation Current\",\"unit\":\"nA\"},{\"symbol\":\"V\",\"name\":\"Voltage\",\"unit\":\"V\"}]",
                "I", "[\"Vt ≈ 26mV at 300K\",\"Calculate exponent: V/Vt\",\"I = Is * (exp(...) - 1)\"]", "Medium"));

        formulas.add(new Formula("Year 2", "ECE", "Electronics", "Diodes",
                "Thermal Voltage", "Vt = kT/q", "k=Boltzmann, T=temp, q=charge", "Volt-equivalent of temp.", "", "", "", ""));
        problems.add(new Problem("Thermal Voltage Solver", "Calculate Vt at specific temperature.", "Electronics", 5,
                "[{\"symbol\":\"T\",\"name\":\"Temperature\",\"unit\":\"K\"}]",
                "Vt", "[\"Vt = (1.38e-23 * T) / 1.6e-19\",\"At 300K, Vt ≈ 25.85mV\"]", "Easy"));

        formulas.add(new Formula("Year 2", "ECE", "Electronics", "Diodes",
                "Junction Capacitance", "Cj = Cj0 / sqrt(1 + Vr/Vbi)", "Vr=reverse bias, Vbi=built-in", "Depletion capacitance.", "", "", "", ""));
        problems.add(new Problem("Junction Capacitance Solver", "Find Cj.", "Electronics", 6,
                "[{\"symbol\":\"Cj0\",\"name\":\"Zero-bias Cap\",\"unit\":\"pF\"},{\"symbol\":\"Vr\",\"name\":\"Reverse Bias\",\"unit\":\"V\"},{\"symbol\":\"Vbi\",\"name\":\"Built-in Pot\",\"unit\":\"V\"}]",
                "Cj", "[\"Cj = Cj0 / sqrt(1 + Vr/Vbi)\"]", "Medium"));

        // ── 3. RECTIFIERS ──────────────────────────────────────────────────
        formulas.add(new Formula("Year 2", "ECE", "Electronics", "Rectifiers",
                "Ripple Factor", "r = sqrt((Irms/Idc)^2 - 1)", "r=ripple", "Measures AC in DC output.", "", "", "", ""));
        problems.add(new Problem("Ripple Factor Solver", "Find r from RMS and DC values.", "Electronics", 7,
                "[{\"symbol\":\"Vrms\",\"name\":\"RMS Voltage\",\"unit\":\"V\"},{\"symbol\":\"Vdc\",\"name\":\"DC Voltage\",\"unit\":\"V\"}]",
                "r", "[\"r = V_ripple_rms / Vdc\",\"Alternatively: r = sqrt((Vrms/Vdc)² - 1)\"]", "Medium"));

        formulas.add(new Formula("Year 2", "ECE", "Electronics", "Rectifiers",
                "Rectifier Efficiency", "η = Pdc / Pac", "η=efficiency", "Power conversion ratio.", "", "", "", ""));
        problems.add(new Problem("Rectifier Efficiency Solver", "Calculate η.", "Electronics", 8,
                "[{\"symbol\":\"Pdc\",\"name\":\"DC Power\",\"unit\":\"W\"},{\"symbol\":\"Pac\",\"name\":\"AC Power\",\"unit\":\"W\"}]",
                "η", "[\"η = (Pdc / Pac) * 100%\"]", "Easy"));

        formulas.add(new Formula("Year 2", "ECE", "Electronics", "Rectifiers",
                "PIV (Bridge)", "PIV = Vm", "Vm=peak voltage", "Peak Inverse Voltage.", "", "", "", ""));
        problems.add(new Problem("PIV Solver", "Find PIV for Bridge Rectifier.", "Electronics", 9,
                "[{\"symbol\":\"Vrms\",\"name\":\"Input RMS\",\"unit\":\"V\"}]",
                "PIV", "[\"Vm = Vrms * sqrt(2)\",\"For Bridge: PIV = Vm\"]", "Easy"));

        // ── 4. BJTs ────────────────────────────────────────────────────────
        formulas.add(new Formula("Year 2", "ECE", "Electronics", "BJT",
                "Common Emitter Gain", "β = α / (1 - α)", "α=CB gain, β=CE gain", "Relationship between gains.", "", "", "", ""));
        problems.add(new Problem("BJT Beta Solver", "Find β from α.", "Electronics", 10,
                "[{\"symbol\":\"α\",\"name\":\"Alpha\",\"unit\":\"\"}]",
                "β", "[\"β = α / (1 - α)\"]", "Easy"));

        formulas.add(new Formula("Year 2", "ECE", "Electronics", "BJT",
                "Emitter Current", "Ie = (1 + β)Ib", "Ib=base current", "Total transistor current.", "", "", "", ""));
        problems.add(new Problem("Emitter Current Solver", "Find Ie.", "Electronics", 11,
                "[{\"symbol\":\"β\",\"name\":\"Beta\",\"unit\":\"\"},{\"symbol\":\"Ib\",\"name\":\"Base Current\",\"unit\":\"μA\"}]",
                "Ie", "[\"Ie = (1 + β) * Ib\"]", "Easy"));

        formulas.add(new Formula("Year 2", "ECE", "Electronics", "BJT",
                "Transconductance", "gm = Ic / Vt", "Ic=collector current", "BJT small-signal gain.", "", "", "", ""));
        problems.add(new Problem("BJT gm Solver", "Calculate gm.", "Electronics", 12,
                "[{\"symbol\":\"Ic\",\"name\":\"Collector Current\",\"unit\":\"mA\"}]",
                "gm", "[\"Assume Vt = 26mV\",\"gm = Ic / 0.026\"]", "Easy"));

        // ── 5. MOSFETs ─────────────────────────────────────────────────────
        formulas.add(new Formula("Year 2", "ECE", "Electronics", "MOSFET",
                "Drain Current (Saturation)", "Id = 0.5 * μn * Cox * (W/L) * (Vgs - Vth)^2", "μn=mobility, Cox=oxide cap", "Square law.", "", "", "", ""));
        problems.add(new Problem("MOSFET Saturation Current", "Calculate Id.", "Electronics", 13,
                "[{\"symbol\":\"K\",\"name\":\"k' (W/L)\",\"unit\":\"mA/V²\"},{\"symbol\":\"Vgs\",\"name\":\"Gate-Source\",\"unit\":\"V\"},{\"symbol\":\"Vth\",\"name\":\"Threshold\",\"unit\":\"V\"}]",
                "Id", "[\"Id = 0.5 * K * (Vgs - Vth)²\"]", "Medium"));

        formulas.add(new Formula("Year 2", "ECE", "Electronics", "MOSFET",
                "Transconductance", "gm = 2 * Id / (Vgs - Vth)", "gm=gain", "MOSFET gain param.", "", "", "", ""));
        problems.add(new Problem("MOSFET gm Solver", "Find gm.", "Electronics", 14,
                "[{\"symbol\":\"Id\",\"name\":\"Drain Current\",\"unit\":\"mA\"},{\"symbol\":\"Vov\",\"name\":\"Overdrive (Vgs-Vth)\",\"unit\":\"V\"}]",
                "gm", "[\"gm = 2 * Id / Vov\"]", "Easy"));

        // ── 6. AMPLIFIERS ──────────────────────────────────────────────────
        formulas.add(new Formula("Year 2", "ECE", "Electronics", "Amplifiers",
                "Voltage Gain", "Av = Vo / Vi", "Vo=output, Vi=input", "Direct gain ratio.", "", "", "", ""));
        problems.add(new Problem("Voltage Gain Solver", "Find Av.", "Electronics", 15,
                "[{\"symbol\":\"Vi\",\"name\":\"Input\",\"unit\":\"mV\"},{\"symbol\":\"Vo\",\"name\":\"Output\",\"unit\":\"V\"}]",
                "Av", "[\"Convert units to be same\",\"Av = Vo / Vi\"]", "Easy"));

        formulas.add(new Formula("Year 2", "ECE", "Electronics", "Amplifiers",
                "Gain in dB", "Gain_dB = 20 * log10(Av)", "Av=linear gain", "Logarithmic gain.", "", "", "", ""));
        problems.add(new Problem("Gain dB Solver", "Convert linear gain to decibels.", "Electronics", 16,
                "[{\"symbol\":\"Av\",\"name\":\"Linear Gain\",\"unit\":\"\"}]",
                "G_dB", "[\"G_dB = 20 * log10(Av)\"]", "Easy"));

        // ── 7. OP-AMPS ─────────────────────────────────────────────────────
        formulas.add(new Formula("Year 3", "ECE", "Analog Circuits", "Op-Amps",
                "Inverting Gain", "Av = -Rf / Ri", "Rf=feedback, Ri=input", "Inverting config gain.", "", "", "", ""));
        problems.add(new Problem("Inverting Op-Amp Solver", "Find Rf for a target gain.", "Analog Circuits", 17,
                "[{\"symbol\":\"Av\",\"name\":\"Target Gain\",\"unit\":\"\"},{\"symbol\":\"Ri\",\"name\":\"Input Res\",\"unit\":\"kΩ\"}]",
                "Rf", "[\"Rf = |Av| * Ri\"]", "Easy"));

        formulas.add(new Formula("Year 3", "ECE", "Analog Circuits", "Op-Amps",
                "Integrator Output", "Vo = -(1/RC) * ∫ Vi dt", "R,C=params", "Op-amp integrator.", "", "", "", ""));
        problems.add(new Problem("Op-Amp Integrator Solver", "Find output voltage over time t.", "Analog Circuits", 18,
                "[{\"symbol\":\"Vi\",\"name\":\"Input Step\",\"unit\":\"V\"},{\"symbol\":\"R\",\"name\":\"Resistor\",\"unit\":\"kΩ\"},{\"symbol\":\"C\",\"name\":\"Capacitor\",\"unit\":\"μF\"},{\"symbol\":\"t\",\"name\":\"Time\",\"unit\":\"ms\"}]",
                "Vo", "[\"Vo = -(Vi * t) / (R * C)\",\"Ensure R*C and t units match\"]", "Hard"));

        // ── 8. OSCILLATORS ─────────────────────────────────────────────────
        formulas.add(new Formula("Year 3", "ECE", "Analog Circuits", "Oscillators",
                "Barkhausen Criterion", "Aβ = 1", "A=gain, β=feedback", "Loop gain condition.", "", "", "", ""));
        problems.add(new Problem("Barkhausen Solver", "Find minimum gain A for oscillation.", "Analog Circuits", 19,
                "[{\"symbol\":\"β\",\"name\":\"Feedback Factor\",\"unit\":\"\"}]",
                "A", "[\"A = 1 / β\"]", "Easy"));

        formulas.add(new Formula("Year 3", "ECE", "Analog Circuits", "Oscillators",
                "Wien Bridge Frequency", "f = 1 / (2πRC)", "R,C=frequency selection", "Resonant frequency.", "", "", "", ""));
        problems.add(new Problem("Wien Bridge Solver", "Calculate frequency f.", "Analog Circuits", 20,
                "[{\"symbol\":\"R\",\"name\":\"R\",\"unit\":\"kΩ\"},{\"symbol\":\"C\",\"name\":\"C\",\"unit\":\"nF\"}]",
                "f", "[\"f = 1 / (2 * π * R * C)\"]", "Medium"));

        // ── 9. FILTERS ─────────────────────────────────────────────────────
        formulas.add(new Formula("Year 2", "ECE", "Network Theory", "Filters",
                "Cutoff Frequency (RC)", "fc = 1 / (2πRC)", "fc=3dB frequency", "Low pass / High pass cutoff.", "", "", "", ""));
        problems.add(new Problem("Filter Cutoff Solver", "Find fc for an RC filter.", "Network Theory", 21,
                "[{\"symbol\":\"R\",\"name\":\"R\",\"unit\":\"Ω\"},{\"symbol\":\"C\",\"name\":\"C\",\"unit\":\"μF\"}]",
                "fc", "[\"fc = 1 / (2 * π * R * C)\"]", "Easy"));

        formulas.add(new Formula("Year 2", "ECE", "Network Theory", "Filters",
                "Quality Factor", "Q = fr / BW", "fr=resonant, BW=bandwidth", "Filter selectivity.", "", "", "", ""));
        problems.add(new Problem("Q-Factor Solver", "Calculate Q from bandwidth.", "Network Theory", 22,
                "[{\"symbol\":\"fr\",\"name\":\"Resonant Freq\",\"unit\":\"kHz\"},{\"symbol\":\"BW\",\"name\":\"Bandwidth\",\"unit\":\"kHz\"}]",
                "Q", "[\"Q = fr / BW\"]", "Easy"));

        // ── 10. DIGITAL ELECTRONICS ────────────────────────────────────────
        formulas.add(new Formula("Year 2", "ECE", "Digital Electronics", "Boolean",
                "De Morgan's Theorem", "(A+B)' = A'B'", "Complement of sum", "Logic simplification.", "", "", "", ""));
        problems.add(new Problem("De Morgan Proof", "Apply De Morgan's to (A.B)'.", "Digital Electronics", 23,
                "[]",
                "Result", "[\"(A . B)' = A' + B'\"]", "Easy"));

        formulas.add(new Formula("Year 2", "ECE", "Digital Electronics", "Combinational",
                "Full Adder Carry", "Cout = AB + Cin(A ⊕ B)", "A,B,Cin=inputs", "Logic for carry out.", "", "", "", ""));
        problems.add(new Problem("Full Adder Logic", "Find Cout given A, B, Cin.", "Digital Electronics", 24,
                "[{\"symbol\":\"A\",\"name\":\"A\",\"unit\":\"\"},{\"symbol\":\"B\",\"name\":\"B\",\"unit\":\"\"},{\"symbol\":\"Cin\",\"name\":\"Cin\",\"unit\":\"\"}]",
                "Cout", "[\"Calculate X = A ⊕ B\",\"Cout = (A AND B) OR (Cin AND X)\"]", "Medium"));

        formulas.add(new Formula("Year 2", "ECE", "Digital Electronics", "Sequential",
                "JK Flip-Flop Eq", "Q+ = JQ' + K'Q", "J,K=inputs", "Next state equation.", "", "", "", ""));
        problems.add(new Problem("JK Next State", "Find Q(n+1) given J, K, Q(n).", "Digital Electronics", 25,
                "[{\"symbol\":\"J\",\"name\":\"J\",\"unit\":\"\"},{\"symbol\":\"K\",\"name\":\"K\",\"unit\":\"\"},{\"symbol\":\"Q\",\"name\":\"Present State\",\"unit\":\"\"}]",
                "Q_next", "[\"Apply Q+ = J.Q' + K'.Q\"]", "Medium"));

        // ── 11. COMMUNICATION SYSTEMS ──────────────────────────────────────
        formulas.add(new Formula("Year 3", "ECE", "Communication", "Sampling",
                "Nyquist Rate", "fs = 2 * fm", "fm=max signal freq", "Minimum sampling frequency.", "", "", "", ""));
        problems.add(new Problem("Nyquist Rate Solver", "Find min sampling rate.", "Communication", 26,
                "[{\"symbol\":\"fm\",\"name\":\"Max Freq\",\"unit\":\"kHz\"}]",
                "fs", "[\"fs = 2 * fm\"]", "Easy"));

        formulas.add(new Formula("Year 3", "ECE", "Communication", "Information",
                "Shannon Capacity", "C = B * log2(1 + SNR)", "B=bandwidth", "Max data rate.", "", "", "", ""));
        problems.add(new Problem("Shannon Capacity Solver", "Calculate max channel capacity.", "Communication", 27,
                "[{\"symbol\":\"B\",\"name\":\"Bandwidth\",\"unit\":\"MHz\"},{\"symbol\":\"SNR_dB\",\"name\":\"SNR in dB\",\"unit\":\"dB\"}]",
                "C", "[\"Convert SNR_dB to linear: SNR = 10^(SNR_dB/10)\",\"C = B * log2(1 + SNR)\"]", "Hard"));

        // ── 12. SIGNALS & TRANSFORMS ───────────────────────────────────────
        formulas.add(new Formula("Year 3", "ECE", "Signals", "Fourier",
                "Fourier Transform Pair", "x(t) ↔ X(ω)", "", "Time-Freq conversion.", "", "", "", ""));
        problems.add(new Problem("Fourier: Rect Pulse", "Find X(ω) for a pulse of width T.", "Signals", 28,
                "[{\"symbol\":\"A\",\"name\":\"Amplitude\",\"unit\":\"\"},{\"symbol\":\"T\",\"name\":\"Width\",\"unit\":\"s\"}]",
                "X(ω)", "[\"X(ω) = A * T * sinc(ωT/2π)\"]", "Medium"));

        // ── 13. TRANSMISSION LINES ─────────────────────────────────────────
        formulas.add(new Formula("Year 3", "ECE", "Electromagnetics", "TL",
                "Characteristic Impedance", "Z0 = sqrt(L/C)", "L,C=per unit", "Lossless line impedance.", "", "", "", ""));
        problems.add(new Problem("Z0 Solver", "Find Z0 of a lossless line.", "Electromagnetics", 29,
                "[{\"symbol\":\"L\",\"name\":\"Inductance\",\"unit\":\"μH/m\"},{\"symbol\":\"C\",\"name\":\"Capacitance\",\"unit\":\"pF/m\"}]",
                "Z0", "[\"Z0 = sqrt(L / C)\",\"Ensure units (e.g., 10^-6 and 10^-12)\"]", "Medium"));

        // ── 14. ADC/DAC ────────────────────────────────────────────────────
        formulas.add(new Formula("Year 3", "ECE", "Digital Circuits", "Data Converters",
                "Quantization Step", "Δ = Vref / 2^n", "n=bits", "LSB voltage.", "", "", "", ""));
        problems.add(new Problem("ADC Step Size", "Find LSB voltage Δ.", "Digital Circuits", 30,
                "[{\"symbol\":\"Vref\",\"name\":\"Ref Voltage\",\"unit\":\"V\"},{\"symbol\":\"n\",\"name\":\"Resolution\",\"unit\":\"bits\"}]",
                "Δ", "[\"Δ = Vref / (2^n)\"]", "Easy"));

        // ── 15. NETWORK THEORY ─────────────────────────────────────────────
        formulas.add(new Formula("Year 2", "Common", "Network Theory", "Theorems",
                "Thevenin Resistance", "Rth = Voc / Isc", "Voc=open circuit, Isc=short circuit", "Network simplification.", "", "", "", ""));
        problems.add(new Problem("Thevenin Solver", "Find Rth from Voc and Isc.", "Network Theory", 31,
                "[{\"symbol\":\"Voc\",\"name\":\"Open Circuit V\",\"unit\":\"V\"},{\"symbol\":\"Isc\",\"name\":\"Short Circuit I\",\"unit\":\"A\"}]",
                "Rth", "[\"Rth = Voc / Isc\"]", "Easy"));

        // ── 16. CONTROL SYSTEMS ────────────────────────────────────────────
        formulas.add(new Formula("Year 3", "ECE", "Control", "Feedback",
                "Closed Loop Transfer Function", "T(s) = G(s)/(1 + G(s)H(s))", "G=forward, H=feedback", "System gain.", "", "", "", ""));
        problems.add(new Problem("Closed Loop Solver", "Find system gain T for constant G, H.", "Control", 32,
                "[{\"symbol\":\"G\",\"name\":\"Forward Gain\",\"unit\":\"\"},{\"symbol\":\"H\",\"name\":\"Feedback Gain\",\"unit\":\"\"}]",
                "T", "[\"T = G / (1 + G*H)\"]", "Easy"));

        // ── 17. MICROPROCESSORS ────────────────────────────────────────────
        formulas.add(new Formula("Year 3", "ECE", "Microprocessors", "Timing",
                "Execution Time", "T = N * (1 / f)", "N=T-states, f=clock", "Instruction duration.", "", "", "", ""));
        problems.add(new Problem("Instruction Timing Solver", "Calculate execution time.", "Microprocessors", 33,
                "[{\"symbol\":\"N\",\"name\":\"T-States\",\"unit\":\"\"},{\"symbol\":\"f\",\"name\":\"Clock Freq\",\"unit\":\"MHz\"}]",
                "T", "[\"T = N / f\",\"Result will be in microseconds if f is in MHz\"]", "Easy"));

        // ── 18. ANTENNAS ───────────────────────────────────────────────────
        formulas.add(new Formula("Year 4", "ECE", "Antennas", "Parameters",
                "Antenna Gain", "G = ηD", "η=efficiency, D=directivity", "Effective power gain.", "", "", "", ""));
        problems.add(new Problem("Antenna Gain Solver", "Find G from D and efficiency.", "Antennas", 34,
                "[{\"symbol\":\"η\",\"name\":\"Efficiency\",\"unit\":\"%\"},{\"symbol\":\"D\",\"name\":\"Directivity\",\"unit\":\"dB\"}]",
                "G_dB", "[\"Convert D from dB if needed, or work in dB\",\"G_dB = 10*log10(η) + D_dB\"]", "Medium"));

        // ── 19. DSP ────────────────────────────────────────────────────────
        formulas.add(new Formula("Year 3", "ECE", "DSP", "DFT",
                "DFT Equation", "X[k] = Σ x[n]e^(-j2πnk/N)", "N=length", "Discrete transformation.", "", "", "", ""));
        problems.add(new Problem("DFT 2-Point Solver", "Find X[0], X[1] for x[n]={a, b}.", "DSP", 35,
                "[{\"symbol\":\"a\",\"name\":\"x[0]\",\"unit\":\"\"},{\"symbol\":\"b\",\"name\":\"x[1]\",\"unit\":\"\"}]",
                "X[k]", "[\"X[0] = a + b\",\"X[1] = a - b\"]", "Medium"));

        // ── 20. COMPUTER NETWORKS ──────────────────────────────────────────
        formulas.add(new Formula("Year 4", "CSE", "Networks", "Link Layer",
                "Efficiency (Sliding Window)", "η = N / (1 + 2a)", "N=window, a=delay ratio", "Protocol performance.", "", "", "", ""));
        problems.add(new Problem("Sliding Window Efficiency", "Find η.", "Networks", 36,
                "[{\"symbol\":\"N\",\"name\":\"Window Size\",\"unit\":\"\"},{\"symbol\":\"a\",\"name\":\"Delay Ratio\",\"unit\":\"\"}]",
                "η", "[\"η = N / (1 + 2*a)\",\"If N > 1+2a, η = 1 (100%)\"]", "Medium"));

        db.formulaDAO().insertAllFormulas(formulas);
        db.problemDAO().insertAllProblems(problems);
    }

    private static void seedProblems(SigmaSolveDatabase db) {
        // Handled in seedFormulasAndProblems
    }
}
