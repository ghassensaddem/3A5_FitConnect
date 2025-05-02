<?php
// src/Repository/CommandeRepository.php

namespace App\Repository;

use App\Entity\Commande;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Commande>
 *
 * @method Commande|null find($id, $lockMode = null, $lockVersion = null)
 * @method Commande|null findOneBy(array $criteria, array $orderBy = null)
 * @method Commande[]    findAll()
 * @method Commande[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class CommandeRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Commande::class);
    }

    public function save(Commande $entity, bool $flush = false): void
    {
        $this->getEntityManager()->persist($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function remove(Commande $entity, bool $flush = false): void
    {
        $this->getEntityManager()->remove($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    // Vous pouvez ajouter ici des méthodes personnalisées pour les requêtes spécifiques
    // Par exemple, pour trouver les commandes par statut de paiement :

    /**
     * @return Commande[] Returns an array of Commande objects
     */
    public function findByStatutPaiement(string $statut): array
    {
        return $this->createQueryBuilder('c')
            ->andWhere('c.statutPaiement = :statut')
            ->setParameter('statut', $statut)
            ->orderBy('c.dateCreation', 'DESC')
            ->getQuery()
            ->getResult()
        ;
    }

    // Autre exemple : trouver les commandes récentes
    public function findRecentCommands(int $limit = 10): array
    {
        return $this->createQueryBuilder('c')
            ->orderBy('c.dateCreation', 'DESC')
            ->setMaxResults($limit)
            ->getQuery()
            ->getResult()
        ;
    }


    /*public function findCommandesByClient(Client $client): array
    {
        return $this->createQueryBuilder('c')
            ->andWhere('c.client = :client')
            ->setParameter('client', $client)
         
            ->getQuery()
            ->getResult();
    }
*/
public function sumQuantitiesForClient($client): int
{
    return (int) $this->createQueryBuilder('c')
        ->select('SUM(c.quantite)')
        ->where('c.client = :client')
        ->andWhere('c.etat = :etat')
        ->setParameter('client', $client)
        ->setParameter('etat', 'En attente')
        ->getQuery()
        ->getSingleScalarResult() ?? 0;
}


}