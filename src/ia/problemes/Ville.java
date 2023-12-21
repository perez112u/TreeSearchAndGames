package ia.problemes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Ville extends Point implements Serializable {
    private String nom;
    private int population;

    public Ville(String nom, int population, double latitude, double longitude) {
        super(latitude, longitude);
        this.nom = nom;
        this.population = population;
    }

    public Ville(String nom, int population) {
        super();
        this.nom = nom;
        this.population = population;
    }

    public static Ville getVilleByName(List<Ville> graphe, String nomVille) {
        return graphe.stream().filter(ville -> ville.getNom().equals(nomVille)).findFirst().get();
    }

    public String getNom() {
        return nom;
    }

    public int getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "nom='" + nom + '\'' +
                ", population=" + population +
                ", latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ville)) return false;
        Ville ville = (Ville) o;
        return getNom().equals(ville.getNom());
    }

    public static void serialize(List<Ville> villes) throws IOException {
        FileOutputStream fos = new FileOutputStream("src/Defis/ia.problemes.Defi1/villes.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(villes);
        oos.close();
        fos.close();
    }

    public static void ser(List<Ville> villes,String path) throws IOException {
        BufferedWriter bos = new BufferedWriter(new FileWriter(path));

        for (int i = 0; i < villes.size(); i++) {
            bos.write(villes.get(i).getNom() + "|" + villes.get(i).getPopulation() + "|" + villes.get(i).getLatitude() + "|" + villes.get(i).getLongitude());
            bos.newLine();
        }
        bos.close();
    }

    public static List<Ville> des(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        List<Ville> villes = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] ville = line.split("\\|");
            villes.add(new Ville(ville[0], Integer.parseInt(ville[1]), Double.parseDouble(ville[2]), Double.parseDouble(ville[3])));
        }
        br.close();
        return villes;
    }

}
