public class FormulaFactory {
    //create a singleton which gets returned
    private static FormulaFactory instance = new FormulaFactory();

    //prevents FormulaFactory instances from being created
    private FormulaFactory() {};

    public static FormulaFactory getInstance() {
        return instance;
    }

    public GreatCircleDistance get(String formula) throws Exception {
        if (formula == null) return new Vincenty();

        switch (formula) {
            case "":
            case "1":
                return new Vincenty();
            case "2":
                return new Haversine();
            case "3":
                return new Cosines();
            default:
                throw new Exception();
        }
    }

}
