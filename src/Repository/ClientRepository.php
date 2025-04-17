<?php

namespace App\Repository;

use App\Entity\Client;
use App\Utils\Encrypt;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;
use Doctrine\ORM\QueryBuilder;

class ClientRepository extends ServiceEntityRepository
{

    private $encrypt;

    public function __construct(ManagerRegistry $registry, Encrypt $encrypt)
    {
        parent::__construct($registry, Client::class);
        $this->encrypt = $encrypt;
    }

    public function ajouter(Client $client): void
    {
        $em = $this->getEntityManager();
        $em->persist($client);
        $em->flush();
    }

    public function supprimer(Client $client): void
    {
        $em = $this->getEntityManager();
        $em->remove($client);
        $em->flush();
    }

    public function modifier(Client $client): void
    {
        $em = $this->getEntityManager();
        $em->flush();
    }

    public function afficher(): array
    {
        return $this->findAll();
    }

    public function rechercherParNom(string $nom): array
    {
        return $this->createQueryBuilder('c')
            ->where('c.nom LIKE :nom')
            ->setParameter('nom', '%'.$nom.'%')
            ->getQuery()
            ->getResult();
    }

    public function trouverParEmail(string $email): ?Client
    {
        return $this->findOneBy(['email' => $email]);
    }

    public function emailExiste(string $email): bool
    {
        return (bool) $this->createQueryBuilder('c')
            ->select('COUNT(c.id)')
            ->where('c.email = :email')
            ->setParameter('email', $email)
            ->getQuery()
            ->getSingleScalarResult();
    }


    public function clientExiste(string $email, string $mdp): bool
    {
        $client = $this->createQueryBuilder('c')
            ->select('c')
            ->where('c.email = :email')
            ->setParameter('email', $email)
            ->getQuery()
            ->getOneOrNullResult();

        // Vérifier si le client existe et si le mot de passe est correct
        if ($client && $client->getMdp()=== $this->encrypt->encryptAES($mdp)) {
            return true;
        }

        return false;
    }


   
    public function updateClientInfo(Client $client): void
    {
        $queryBuilder = $this->createQueryBuilder('c')
            ->update(Client::class, 'c')
            ->set('c.nom', ':nom')
            ->set('c.prenom', ':prenom')
            ->set('c.email', ':email')
            ->where('c.id = :id')
            ->setParameter('nom', $client->getNom())
            ->setParameter('prenom', $client->getPrenom())
            ->setParameter('email', $client->getEmail())
            ->setParameter('id', $client->getId());

        // Vérifiez si le mot de passe est non vide avant de le mettre à jour
        $mdp = $client->getMdp();
        if ($mdp !== null && $mdp !== '') {
            $encryptedPassword = $this->encrypt->encryptAES($mdp);
            $queryBuilder->set('c.mdp', ':mdp')
                ->setParameter('mdp', $encryptedPassword);
        }

        $queryBuilder->getQuery()->execute();
    }


    public function updateImage(Client $client): void
    {
        $queryBuilder = $this->createQueryBuilder('c')
            ->update(Client::class, 'c')
            ->set('c.image', ':image')
            ->where('c.id = :id')
            ->setParameter('image', $client->getImage())
            ->setParameter('id', $client->getId());
        $queryBuilder->getQuery()->execute();
    }


    public function AdminUpdateClient(Client $client): void
    {
        $queryBuilder = $this->createQueryBuilder('c')
            ->update(Client::class, 'c')
            ->set('c.nom', ':nom')
            ->set('c.prenom', ':prenom')
            ->set('c.email', ':email')
            ->set('c.dateNaissance', ':dateNaissance')
            ->set('c.poids', ':poids')
            ->set('c.taille', ':taille')
            ->set('c.mdp', ':mdp')
            ->where('c.id = :id')
            ->setParameter('nom', $client->getNom())
            ->setParameter('prenom', $client->getPrenom())
            ->setParameter('email', $client->getEmail())
            ->setParameter('dateNaissance', $client->getDateNaissance())
            ->setParameter('poids', $client->getPoids())
            ->setParameter('taille', $client->getTaille())
            ->setParameter('mdp', $client->getMdp())
            ->setParameter('id', $client->getId());

        $queryBuilder->getQuery()->execute();
    }

    

}
