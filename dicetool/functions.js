function registerFunctions() {
	var map = new java.util.HashMap();
	map.put("test01", test01);
	map.put("test02", test02);

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

