<?php

namespace App\Repository;

use App\Entity\Sallesportif;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Sallesportif>
 *
 * @method Sallesportif|null find($id, $lockMode = null, $lockVersion = null)
 * @method Sallesportif|null findOneBy(array $criteria, array $orderBy = null)
 * @method Sallesportif[]    findAll()
 * @method Sallesportif[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class SallesportifRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Sallesportif::class);
    }

    public function save(Sallesportif $entity, bool $flush = false): void
    {
        $this->getEntityManager()->persist($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    public function remove(Sallesportif $entity, bool $flush = false): void
    {
        $this->getEntityManager()->remove($entity);

        if ($flush) {
            $this->getEntityManager()->flush();
        }
    }

    /**
     * Trouve les salles par capacité minimale
     */
    public function findByMinCapacity(int $capacity): array
    {
        return $this->createQueryBuilder('s')
            ->andWhere('s.capacity >= :capacity')
            ->setParameter('capacity', $capacity)
            ->orderBy('s.capacity', 'ASC')
            ->getQuery()
            ->getResult();
    }

    /**
     * Trouve les salles ouvertes à une heure donnée
     */
    public function findOpenAtTime(\DateTimeInterface $time): array
    {
        return $this->createQueryBuilder('s')
            ->andWhere('s.horaireOuverture <= :time')
            ->andWhere('s.horaireFermeture >= :time')
            ->setParameter('time', $time)
            ->getQuery()
            ->getResult();
    }

    /**
     * Trouve les salles par nom (recherche insensible à la casse)
     */
    public function searchByName(string $name): array
    {
        return $this->createQueryBuilder('s')
            ->andWhere('LOWER(s.nomSalle) LIKE LOWER(:name)')
            ->setParameter('name', '%'.$name.'%')
            ->getQuery()
            ->getResult();
    }

    /**
     * Compte le nombre total de salles
     */
    public function countAll(): int
    {
        return $this->createQueryBuilder('s')
            ->select('COUNT(s.idSalle)')
            ->getQuery()
            ->getSingleScalarResult();
    }
}