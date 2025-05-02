<?php

namespace App\Repository;

use App\Entity\PlanningActivity;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<PlanningActivity>
 */
class PlanningActivityRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, PlanningActivity::class);
    }

    public function save(PlanningActivity $entity, bool $flush = false): void
    {
        $this->getEntityManager()->persist($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function remove(PlanningActivity $entity, bool $flush = false): void
    {
        $this->getEntityManager()->remove($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    /**
     * Recherche par terme dans nomCategorie ou description
     */
    public function findBySearchTerm(string $term): array
    {
        return $this->createQueryBuilder('c')
            ->where('c.nomCategorie LIKE :term')
            ->orWhere('c.description LIKE :term')
            ->setParameter('term', '%'.$term.'%')
            ->orderBy('c.nomCategorie', 'ASC')
            ->getQuery()
            ->getResult();
    }

    /**
     * Trouve une catégorie par son nom exact (insensible à la casse)
     */
    public function findOneByNom(string $nom): ?PlanningActivity
    {
        return $this->createQueryBuilder('c')
            ->where('LOWER(c.nomCategorie) = LOWER(:nom)')
            ->setParameter('nom', $nom)
            ->getQuery()
            ->getOneOrNullResult();
    }

    /**
     * Récupère toutes les catégories triées par nom
     */
    public function findAllOrdered(): array
    {
        return $this->createQueryBuilder('c')
            ->orderBy('c.nomCategorie', 'ASC')
            ->getQuery()
            ->getResult();
    }

    /**
     * Compte le nombre total de catégories
     */
    public function countAll(): int
    {
        return $this->createQueryBuilder('c')
            ->select('COUNT(c.idCategorie)')
            ->getQuery()
            ->getSingleScalarResult();
    }
    public function findAllWithDetails()
{
    return $this->createQueryBuilder('p')
        ->join('p.activity', 'a')
        ->join('p.salle', 's')
        ->addSelect('a')
        ->addSelect('s')
        ->getQuery()
        ->getResult();
}
    //    }
}
