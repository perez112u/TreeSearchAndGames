package ia.problemes;

import ia.framework.common.State;

import java.io.IOException;
import java.util.List;

public class GpsState extends State{
    private final List<Ville> graphe = Ville.des("src/ia/problemes/villes.txt");
    private String nomVille;
    private double lat;
    private double lon;

    public GpsState(String nomVille) throws IOException, ClassNotFoundException {
        Ville v = Ville.getVilleByName(graphe, nomVille);
        if (v.getLatitude() == 0 && v.getLongitude() == 0) throw new IllegalArgumentException("Ville non trouvée");
        this.nomVille = v.getNom();
        this.lat = v.getLatitude();
        this.lon = v.getLongitude();
    }
    public GpsState(String nomVille, double lat, double lon) throws IOException {
        this.nomVille = nomVille;
        this.lat = lat;
        this.lon = lon;
    }



    @Override
    protected State cloneState() throws IOException {
        return new GpsState(nomVille, lat, lon);
    }

    @Override
    protected boolean equalsState(State o) {
        GpsState other = (GpsState) o;
        return this.nomVille.equals(other.nomVille) && this.lat == other.lat && this.lon == other.lon;
    }

    @Override
    protected int hashState() {
        return nomVille.hashCode() + (int) lat + (int) lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return nomVille + '\'' +
                ": [" + lat +
                ", " + lon + "]";
    }
}
