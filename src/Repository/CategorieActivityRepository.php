<?php

namespace App\Repository;

use App\Entity\CategorieActivity;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<CategorieActivity>
 *
 * @method CategorieActivity|null find($id, $lockMode = null, $lockVersion = null)
 * @method CategorieActivity|null findOneBy(array $criteria, array $orderBy = null)
 * @method CategorieActivity[]    findAll()
 * @method CategorieActivity[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class CategorieActivityRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, CategorieActivity::class);
    }

    public function save(CategorieActivity $entity, bool $flush = false): void
    {
        $this->getEntityManager()->persist($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function remove(CategorieActivity $entity, bool $flush = false): void
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
    public function findOneByNom(string $nom): ?CategorieActivity
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
}