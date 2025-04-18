<?php

namespace App\Repository;

use App\Entity\Activiteevent;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\ORM\OptimisticLockException;
use Doctrine\ORM\ORMException;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Activiteevent>
 *
 * @method Activiteevent|null find($id, $lockMode = null, $lockVersion = null)
 * @method Activiteevent|null findOneBy(array $criteria, array $orderBy = null)
 * @method Activiteevent[]    findAll()
 * @method Activiteevent[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class ActiviteeventRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Activiteevent::class);
    }

    /**
     * @throws ORMException
     * @throws OptimisticLockException
     */
    public function add(Activiteevent $entity, bool $flush = true): void
    {
        $this->_em->persist($entity);
        if ($flush) {
            $this->_em->flush();
        }
    }

    /**
     * @throws ORMException
     * @throws OptimisticLockException
     */
    public function remove(Activiteevent $entity, bool $flush = true): void
    {
        $this->_em->remove($entity);
        if ($flush) {
            $this->_em->flush();
        }
    }

    // /**
    //  * @return Activiteevent[] Returns an array of Activiteevent objects
    //  */
    /*
    public function findByExampleField($value)
    {
        return $this->createQueryBuilder('a')
            ->andWhere('a.exampleField = :val')
            ->setParameter('val', $value)
            ->orderBy('a.id', 'ASC')
            ->setMaxResults(10)
            ->getQuery()
            ->getResult()
        ;
    }
    */

    /*
    public function findOneBySomeField($value): ?Activiteevent
    {
        return $this->createQueryBuilder('a')
            ->andWhere('a.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */
}
