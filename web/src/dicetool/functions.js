function registerFunctions() {
	var map = new java.util.HashMap();
	map.put("test01", test01);
	map.put("test02", test02);

	map.put("label", label);
	map.put("l", label);
	
	map.put("rr", rrLabel);
	map.put("manyAttack", manyAttack);
	return map;
}

function test01(a) {
	return a + 12;
}

function test02(a) {
	row.setLabel("In JS");
	row.setForegroundColor(255, 0, 0);
	row.setBackgroundColor(0, 255, 0);
	return a + 1;
}

function label(a, lbl) {
	row.setLabel(lbl);
	return a;
}

function rrLabel(a, lbl) {
	row.setReRollExpression(lbl);
	return a;
}

function manyAttack(number, ab, critRange, ac) {
	var hits = 0;
	var crits = 0;

	for (var i = 0; i < number; i++) {
		var roll = rand.nextInt(20) + 1;
		var critRoll = rand.nextInt(20) + 1;
		if (roll == 20 || roll + ab >= ac) {
			hits++;
			
			if (roll >= critRange && (critRoll == 20 || critRoll + ab >= ac)) {
				crits++;
			}
		}
	}

	if (crits > 0) {
		resultSet.addExpression("crits", String.valueOf(crits), null, crits);
	}
	
	return hits;
}
