<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20250329233450 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE author (id INT AUTO_INCREMENT NOT NULL, username VARCHAR(50) NOT NULL, email VARCHAR(150) NOT NULL, nb_books DOUBLE PRECISION DEFAULT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE book (ref INT NOT NULL, author_id INT DEFAULT NULL, title VARCHAR(255) NOT NULL, date DATE DEFAULT NULL, enabled TINYINT(1) NOT NULL, INDEX IDX_CBE5A331F675F31B (author_id), PRIMARY KEY(ref)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE messenger_messages (id BIGINT AUTO_INCREMENT NOT NULL, body LONGTEXT NOT NULL, headers LONGTEXT NOT NULL, queue_name VARCHAR(190) NOT NULL, created_at DATETIME NOT NULL COMMENT \'(DC2Type:datetime_immutable)\', available_at DATETIME NOT NULL COMMENT \'(DC2Type:datetime_immutable)\', delivered_at DATETIME DEFAULT NULL COMMENT \'(DC2Type:datetime_immutable)\', INDEX IDX_75EA56E0FB7336F0 (queue_name), INDEX IDX_75EA56E0E3BD61CE (available_at), INDEX IDX_75EA56E016BA31DB (delivered_at), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE book ADD CONSTRAINT FK_CBE5A331F675F31B FOREIGN KEY (author_id) REFERENCES author (id)');
        $this->addSql('ALTER TABLE activiteevent DROP FOREIGN KEY fk');
        $this->addSql('ALTER TABLE avis DROP FOREIGN KEY fk15KK6_avis');
        $this->addSql('ALTER TABLE commande DROP FOREIGN KEY fk_client');
        $this->addSql('ALTER TABLE commande_equipement DROP FOREIGN KEY commande_equipement_ibfk_1');
        $this->addSql('ALTER TABLE equipement DROP FOREIGN KEY equipement_ibfk_1');
        $this->addSql('ALTER TABLE programme DROP FOREIGN KEY fk15KK6_prog');
        $this->addSql('ALTER TABLE seance DROP FOREIGN KEY fk15KK6_Seance');
        $this->addSql('ALTER TABLE typeactivite DROP FOREIGN KEY fk3');
        $this->addSql('ALTER TABLE vote DROP FOREIGN KEY vote_ibfk_2');
        $this->addSql('ALTER TABLE vote DROP FOREIGN KEY vote_ibfk_1');
        $this->addSql('ALTER TABLE vote_com DROP FOREIGN KEY vote_com_ibfk_1');
        $this->addSql('ALTER TABLE vote_com DROP FOREIGN KEY vote_com_ibfk_2');
        $this->addSql('DROP TABLE activiteevent');
        $this->addSql('DROP TABLE admin');
        $this->addSql('DROP TABLE avis');
        $this->addSql('DROP TABLE categorie_equipement');
        $this->addSql('DROP TABLE coach');
        $this->addSql('DROP TABLE commande');
        $this->addSql('DROP TABLE commande_equipement');
        $this->addSql('DROP TABLE equipement');
        $this->addSql('DROP TABLE programme');
        $this->addSql('DROP TABLE seance');
        $this->addSql('DROP TABLE typeactivite');
        $this->addSql('DROP TABLE vote');
        $this->addSql('DROP TABLE vote_com');
        $this->addSql('ALTER TABLE client DROP FOREIGN KEY fh');
        $this->addSql('ALTER TABLE client DROP FOREIGN KEY fh');
        $this->addSql('ALTER TABLE client CHANGE idEvent idEvent INT DEFAULT NULL, CHANGE dateNaissance date_naissance VARCHAR(20) NOT NULL');
        $this->addSql('ALTER TABLE client ADD CONSTRAINT FK_C74404552C6A49BA FOREIGN KEY (idEvent) REFERENCES event (id) ON DELETE CASCADE');
        $this->addSql('DROP INDEX fh ON client');
        $this->addSql('CREATE INDEX IDX_C74404552C6A49BA ON client (idEvent)');
        $this->addSql('ALTER TABLE client ADD CONSTRAINT fh FOREIGN KEY (idEvent) REFERENCES event (id) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE commentaire DROP FOREIGN KEY fk15KK6_client');
        $this->addSql('ALTER TABLE commentaire DROP FOREIGN KEY fk15KK6_commentaire');
        $this->addSql('ALTER TABLE commentaire DROP FOREIGN KEY fk15KK6_client');
        $this->addSql('ALTER TABLE commentaire DROP FOREIGN KEY fk15KK6_commentaire');
        $this->addSql('ALTER TABLE commentaire CHANGE post_id post_id INT DEFAULT NULL, CHANGE client_id client_id INT DEFAULT NULL');
        $this->addSql('ALTER TABLE commentaire ADD CONSTRAINT FK_67F068BC4B89032C FOREIGN KEY (post_id) REFERENCES post (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE commentaire ADD CONSTRAINT FK_67F068BC19EB6921 FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE');
        $this->addSql('DROP INDEX fk15kk6_commentaire ON commentaire');
        $this->addSql('CREATE INDEX IDX_67F068BC4B89032C ON commentaire (post_id)');
        $this->addSql('DROP INDEX fk15kk6_client ON commentaire');
        $this->addSql('CREATE INDEX IDX_67F068BC19EB6921 ON commentaire (client_id)');
        $this->addSql('ALTER TABLE commentaire ADD CONSTRAINT fk15KK6_client FOREIGN KEY (client_id) REFERENCES client (id) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE commentaire ADD CONSTRAINT fk15KK6_commentaire FOREIGN KEY (post_id) REFERENCES post (id) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE post DROP FOREIGN KEY fk15KK6_post');
        $this->addSql('ALTER TABLE post DROP FOREIGN KEY fk15KK6_post');
        $this->addSql('ALTER TABLE post CHANGE client_id client_id INT DEFAULT NULL');
        $this->addSql('ALTER TABLE post ADD CONSTRAINT FK_5A8A6C8D19EB6921 FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE');
        $this->addSql('DROP INDEX fk15kk6_post ON post');
        $this->addSql('CREATE INDEX IDX_5A8A6C8D19EB6921 ON post (client_id)');
        $this->addSql('ALTER TABLE post ADD CONSTRAINT fk15KK6_post FOREIGN KEY (client_id) REFERENCES client (id) ON UPDATE CASCADE ON DELETE CASCADE');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE activiteevent (id INT AUTO_INCREMENT NOT NULL, horaire VARCHAR(30) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, nbrparticipant INT NOT NULL, idEvent INT NOT NULL, INDEX fk (idEvent), PRIMARY KEY(id)) DEFAULT CHARACTER SET latin1 COLLATE `latin1_swedish_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE admin (id INT AUTO_INCREMENT NOT NULL, nom VARCHAR(30) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, prenom VARCHAR(40) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, sexe VARCHAR(10) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, mdp VARCHAR(32) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, dateNaissance VARCHAR(20) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, email VARCHAR(30) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, PRIMARY KEY(id)) DEFAULT CHARACTER SET latin1 COLLATE `latin1_swedish_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE avis (id INT AUTO_INCREMENT NOT NULL, seanceid INT NOT NULL, commentaire VARCHAR(100) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, note INT NOT NULL, INDEX fk15KK6_avis (seanceid), PRIMARY KEY(id)) DEFAULT CHARACTER SET latin1 COLLATE `latin1_swedish_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE categorie_equipement (id INT AUTO_INCREMENT NOT NULL, nom VARCHAR(255) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, description VARCHAR(255) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, PRIMARY KEY(id)) DEFAULT CHARACTER SET latin1 COLLATE `latin1_swedish_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE coach (id INT AUTO_INCREMENT NOT NULL, nom VARCHAR(10) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, prenom VARCHAR(10) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, sexe VARCHAR(10) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, mdp VARCHAR(10) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, dateNaissance VARCHAR(20) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, email VARCHAR(20) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, lieuEngagement VARCHAR(50) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, specialite VARCHAR(20) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, PRIMARY KEY(id)) DEFAULT CHARACTER SET latin1 COLLATE `latin1_swedish_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE commande (id INT AUTO_INCREMENT NOT NULL, client_id INT NOT NULL, etat VARCHAR(50) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, date_livraison DATETIME DEFAULT NULL, statut_paiement VARCHAR(50) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, INDEX fk_client (client_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET latin1 COLLATE `latin1_swedish_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE commande_equipement (id INT AUTO_INCREMENT NOT NULL, commande_id INT NOT NULL, equipement_id INT NOT NULL, quantite INT NOT NULL, prix_unitaire NUMERIC(10, 2) NOT NULL, INDEX commande_id (commande_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET latin1 COLLATE `latin1_swedish_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE equipement (id INT AUTO_INCREMENT NOT NULL, categorie_id INT DEFAULT NULL, nom VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL COLLATE `utf8mb4_general_ci`, description VARCHAR(255) CHARACTER SET utf8mb4 DEFAULT NULL COLLATE `utf8mb4_general_ci`, prix NUMERIC(10, 2) NOT NULL, quantite_stock INT NOT NULL, INDEX categorie_id (categorie_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET latin1 COLLATE `latin1_swedish_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE programme (id INT AUTO_INCREMENT NOT NULL, coach_id INT NOT NULL, datedebut DATE NOT NULL, datefin DATE NOT NULL, prix DOUBLE PRECISION NOT NULL, description VARCHAR(250) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, INDEX fk15KK6_prog (coach_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET latin1 COLLATE `latin1_swedish_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE seance (id INT AUTO_INCREMENT NOT NULL, programme_id INT NOT NULL, date DATE NOT NULL, horaire TIME NOT NULL, lieu VARCHAR(250) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, INDEX fk15KK6_Seance (programme_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET latin1 COLLATE `latin1_swedish_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE typeactivite (id INT AUTO_INCREMENT NOT NULL, title VARCHAR(30) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, description VARCHAR(250) CHARACTER SET latin1 NOT NULL COLLATE `latin1_swedish_ci`, idActivite INT NOT NULL, INDEX fk3 (idActivite), PRIMARY KEY(id)) DEFAULT CHARACTER SET latin1 COLLATE `latin1_swedish_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE vote (id INT AUTO_INCREMENT NOT NULL, post_id INT NOT NULL, client_id INT NOT NULL, vote_type INT DEFAULT NULL, INDEX post_id (post_id), INDEX client_id (client_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('CREATE TABLE vote_com (id INT AUTO_INCREMENT NOT NULL, comment_id INT NOT NULL, client_id INT NOT NULL, vote_type TINYINT(1) NOT NULL, INDEX comment_id (comment_id), INDEX client_id (client_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_general_ci` ENGINE = InnoDB COMMENT = \'\' ');
        $this->addSql('ALTER TABLE activiteevent ADD CONSTRAINT fk FOREIGN KEY (idEvent) REFERENCES event (id) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE avis ADD CONSTRAINT fk15KK6_avis FOREIGN KEY (seanceid) REFERENCES seance (id) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE commande ADD CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE commande_equipement ADD CONSTRAINT commande_equipement_ibfk_1 FOREIGN KEY (commande_id) REFERENCES commande (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE equipement ADD CONSTRAINT equipement_ibfk_1 FOREIGN KEY (categorie_id) REFERENCES categorie_equipement (id) ON UPDATE CASCADE ON DELETE SET NULL');
        $this->addSql('ALTER TABLE programme ADD CONSTRAINT fk15KK6_prog FOREIGN KEY (coach_id) REFERENCES coach (id) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE seance ADD CONSTRAINT fk15KK6_Seance FOREIGN KEY (programme_id) REFERENCES programme (id) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE typeactivite ADD CONSTRAINT fk3 FOREIGN KEY (idActivite) REFERENCES activiteevent (id)');
        $this->addSql('ALTER TABLE vote ADD CONSTRAINT vote_ibfk_2 FOREIGN KEY (client_id) REFERENCES client (id)');
        $this->addSql('ALTER TABLE vote ADD CONSTRAINT vote_ibfk_1 FOREIGN KEY (post_id) REFERENCES post (id)');
        $this->addSql('ALTER TABLE vote_com ADD CONSTRAINT vote_com_ibfk_1 FOREIGN KEY (comment_id) REFERENCES commentaire (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE vote_com ADD CONSTRAINT vote_com_ibfk_2 FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE book DROP FOREIGN KEY FK_CBE5A331F675F31B');
        $this->addSql('DROP TABLE author');
        $this->addSql('DROP TABLE book');
        $this->addSql('DROP TABLE messenger_messages');
        $this->addSql('ALTER TABLE client DROP FOREIGN KEY FK_C74404552C6A49BA');
        $this->addSql('ALTER TABLE client DROP FOREIGN KEY FK_C74404552C6A49BA');
        $this->addSql('ALTER TABLE client CHANGE idEvent idEvent INT NOT NULL, CHANGE date_naissance dateNaissance VARCHAR(20) NOT NULL');
        $this->addSql('ALTER TABLE client ADD CONSTRAINT fh FOREIGN KEY (idEvent) REFERENCES event (id) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('DROP INDEX idx_c74404552c6a49ba ON client');
        $this->addSql('CREATE INDEX fh ON client (idEvent)');
        $this->addSql('ALTER TABLE client ADD CONSTRAINT FK_C74404552C6A49BA FOREIGN KEY (idEvent) REFERENCES event (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE commentaire DROP FOREIGN KEY FK_67F068BC4B89032C');
        $this->addSql('ALTER TABLE commentaire DROP FOREIGN KEY FK_67F068BC19EB6921');
        $this->addSql('ALTER TABLE commentaire DROP FOREIGN KEY FK_67F068BC4B89032C');
        $this->addSql('ALTER TABLE commentaire DROP FOREIGN KEY FK_67F068BC19EB6921');
        $this->addSql('ALTER TABLE commentaire CHANGE post_id post_id INT NOT NULL, CHANGE client_id client_id INT NOT NULL');
        $this->addSql('ALTER TABLE commentaire ADD CONSTRAINT fk15KK6_client FOREIGN KEY (client_id) REFERENCES client (id) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('ALTER TABLE commentaire ADD CONSTRAINT fk15KK6_commentaire FOREIGN KEY (post_id) REFERENCES post (id) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('DROP INDEX idx_67f068bc4b89032c ON commentaire');
        $this->addSql('CREATE INDEX fk15KK6_commentaire ON commentaire (post_id)');
        $this->addSql('DROP INDEX idx_67f068bc19eb6921 ON commentaire');
        $this->addSql('CREATE INDEX fk15KK6_client ON commentaire (client_id)');
        $this->addSql('ALTER TABLE commentaire ADD CONSTRAINT FK_67F068BC4B89032C FOREIGN KEY (post_id) REFERENCES post (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE commentaire ADD CONSTRAINT FK_67F068BC19EB6921 FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE post DROP FOREIGN KEY FK_5A8A6C8D19EB6921');
        $this->addSql('ALTER TABLE post DROP FOREIGN KEY FK_5A8A6C8D19EB6921');
        $this->addSql('ALTER TABLE post CHANGE client_id client_id INT NOT NULL');
        $this->addSql('ALTER TABLE post ADD CONSTRAINT fk15KK6_post FOREIGN KEY (client_id) REFERENCES client (id) ON UPDATE CASCADE ON DELETE CASCADE');
        $this->addSql('DROP INDEX idx_5a8a6c8d19eb6921 ON post');
        $this->addSql('CREATE INDEX fk15KK6_post ON post (client_id)');
        $this->addSql('ALTER TABLE post ADD CONSTRAINT FK_5A8A6C8D19EB6921 FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE');
    }
}
