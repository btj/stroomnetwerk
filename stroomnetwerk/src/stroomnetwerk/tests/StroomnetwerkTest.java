package stroomnetwerk.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import stroomnetwerk.Knoop;
import stroomnetwerk.Leiding;

class StroomnetwerkTest {

	@Test
	void test() {
		Knoop k1 = new Knoop();
		assertEquals(Set.of(), k1.getInkomendeLeidingen());
		assertEquals(Set.of(), k1.getUitgaandeLeidingen());
		
		Leiding l1 = new Leiding(5);
		assertEquals(5, l1.getDebiet());
		assertEquals(Set.of(), l1.getGekoppeldeOnderdelen());
		
		Knoop k2 = new Knoop();
		l1.koppelAan(k1, k2);
		assertEquals(k1, l1.getBronknoop());
		assertEquals(k2, l1.getDoelknoop());
		assertEquals(Set.of(), k1.getInkomendeLeidingen());
		assertEquals(Set.of(l1), k1.getUitgaandeLeidingen());
		assertEquals(Set.of(l1), k2.getInkomendeLeidingen());
		assertEquals(Set.of(), k2.getUitgaandeLeidingen());
		
		Knoop k3 = new Knoop();
		Leiding l2 = new Leiding(7);
		l2.koppelAan(k2, k3);
		assertEquals(Set.of(l1), k2.getInkomendeLeidingen());
		assertEquals(Set.of(l2), k2.getUitgaandeLeidingen());
		
		Leiding l3 = new Leiding(5);
		l3.koppelAan(k3, k2);
		assertEquals(Set.of(l1, l3), k2.getInkomendeLeidingen());
		l3.ontkoppel();
		assertEquals(Set.of(), k3.getUitgaandeLeidingen());
		assertEquals(Set.of(l1), k2.getInkomendeLeidingen());
		
		Knoop k4 = new Knoop();
		l3.koppelAan(k2, k4);
		
		assertEquals(Set.of(l1, l2, l3), k2.getGekoppeldeOnderdelen());
		assertEquals(Set.of(k2, k4), l3.getGekoppeldeOnderdelen());
		
		assertTrue(k3.isGelijkaardigAan(k4));
		assertFalse(k3.isGelijkaardigAan(k2));
		assertTrue(l1.isGelijkaardigAan(l3));
		assertFalse(l2.isGelijkaardigAan(l3));
		assertFalse(l2.isGelijkaardigAan(k4));
		assertFalse(k4.isGelijkaardigAan(l2));
		
		Set<Knoop> k2OnmiddellijkStroomafwaartseKnopen = new HashSet<>();
		for (Iterator<Knoop> i = k2.getOnmiddellijkStroomafwaartseKnopenIterator(); i.hasNext(); )
			k2OnmiddellijkStroomafwaartseKnopen.add(i.next());
		assertEquals(Set.of(k3, k4), k2OnmiddellijkStroomafwaartseKnopen);
		
		Set<Knoop> k2TegenoverliggendeKnopen = new HashSet<>();
		k2.forEachTegenoverliggendeKnoop(k -> k2TegenoverliggendeKnopen.add(k));
		assertEquals(Set.of(k1, k3, k4), k2TegenoverliggendeKnopen);
		
		assertEquals(Set.of(k2, k3, k4), k1.getStroomafwaartseKnopenStream().collect(Collectors.toSet()));
	}

}
