    package com.esprit.models;

    import java.time.LocalTime;

    public class SalleSportif {
        private int idSalle;
        private String nomSalle;
        private String addresseSalle;
        private LocalTime HoraireOuverture;
        private LocalTime HoraireFermeture;
        private int Capacity;


        public SalleSportif(int idSalle, String nomSalle, String addresseSalle, LocalTime horaireOuverture, LocalTime horaireFermeture, int capacity) {
            this.idSalle = idSalle;
            this.nomSalle = nomSalle;
            this.addresseSalle = addresseSalle;
            HoraireOuverture = horaireOuverture;
            HoraireFermeture = horaireFermeture;
            Capacity = capacity;
        }
        public int getIdSalle() {
            return idSalle;
        }

        public void setIdSalle(int idSalle) {
            this.idSalle = idSalle;
        }

        public String getNomSalle() {
            return nomSalle;
        }

        public void setNomSalle(String nomSalle) {
            this.nomSalle = nomSalle;
        }

        public String getAddresseSalle() {
            return addresseSalle;
        }

        public void setAddresseSalle(String addresseSalle) {
            this.addresseSalle = addresseSalle;
        }

        public LocalTime getHoraireOuverture() {
            return HoraireOuverture;
        }

        public void setHoraireOuverture(LocalTime horaireOuverture) {
            HoraireOuverture = horaireOuverture;
        }

        public LocalTime getHoraireFermeture() {
            return HoraireFermeture;
        }

        public void setHoraireFermeture(LocalTime horaireFermeture) {
            HoraireFermeture = horaireFermeture;
        }

        public int getCapacity() {
            return Capacity;
        }

        public void setCapacity(int capacity) {
            Capacity = capacity;
        }

        @Override
        public String toString() {
            return "SalleSportif{" +
                    "idSalle=" + idSalle +
                    ", nomSalle='" + nomSalle + '\'' +
                    ", addresseSalle='" + addresseSalle + '\'' +
                    ", HoraireOuverture='" + HoraireOuverture + '\'' +
                    ", HoraireFermeture='" + HoraireFermeture + '\'' +
                    ", Capacity='" + Capacity + '\'' +
                    '}';
        }
    }
