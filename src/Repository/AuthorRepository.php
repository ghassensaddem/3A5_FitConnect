<?php

namespace App\Repository;

use App\Entity\Author;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Author>
 */
class AuthorRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Author::class);
    }


    public function chercherauthor()
    {
        $entityManager = $this->getEntityManager();
        $query = $entityManager
            ->createQuery("SELECT a FROM APP\Entity\Author a JOIN a.books b WHERE b.title LIKE '%ESPRIT'");
            
        return $query->getResult();
    }


    
    

    public function chercherauthordql($titleSuffix)
{
    $entityManager = $this->getEntityManager();
    
    // Requête DQL pour sélectionner les auteurs avec des livres dont le titre contient le mot donné
    $query = $entityManager->createQuery(
        "SELECT a 
         FROM App\Entity\Author a 
         JOIN a.books b 
         WHERE b.title LIKE :title"
    )->setParameter('title', '%' . $titleSuffix . '%'); // Ajout du paramètre dynamique
    
    return $query->getResult();
}

public function findAuthorsWithMoreThanTwentyBooks()
{
    $entityManager = $this->getEntityManager();
    
    // Requête DQL pour sélectionner les auteurs avec plus de 20 livres
    $query = $entityManager->createQuery(
        "SELECT a
         FROM App\Entity\Author a
         JOIN a.books b
         GROUP BY a.id
         HAVING COUNT(b.id) > 20"
    );

    return $query->getResult();
}



    

    //    /**
    //     * @return Author[] Returns an array of Author objects
    //     */
    //    public function findByExampleField($value): array
    //    {
    //        return $this->createQueryBuilder('a')
    //            ->andWhere('a.exampleField = :val')
    //            ->setParameter('val', $value)
    //            ->orderBy('a.id', 'ASC')
    //            ->setMaxResults(10)
    //            ->getQuery()
    //            ->getResult()
    //        ;
    //    }

    //    public function findOneBySomeField($value): ?Author
    //    {
    //        return $this->createQueryBuilder('a')
    //            ->andWhere('a.exampleField = :val')
    //            ->setParameter('val', $value)
    //            ->getQuery()
    //            ->getOneOrNullResult()
    //        ;
    //    }
}
