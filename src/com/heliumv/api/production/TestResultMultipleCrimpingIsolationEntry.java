package com.heliumv.api.production;

public class TestResultMultipleCrimpingIsolationEntry extends
		TestResultCrimpingIsolationEntry {
	// notwendig, da Jackson ueber das JsonSubTypes-Mapping (siehe TestResultEntry)
	// mehrere Types nicht auf die gleichen Klassen mappen kann (geht erst ab Version 2.6.0)
}