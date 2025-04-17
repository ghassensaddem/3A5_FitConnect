<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Attribute\Route;
use App\Repository\BookRepository;
use Doctrine\Persistence\ManagerRegistry;
use App\Form;
use App\Entity\Book;
use App\Form\BookType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;



class BookController extends AbstractController
{

    #[Route('/Bookajout', name: 'Book_ajout')]
public function ajoutB(Request $request, ManagerRegistry $doctrine): Response
{
    $book = new Book();
    $form = $this->createForm(BookType::class, $book);

    // Traiter la requête du formulaire
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        // Récupérer le gestionnaire d'entité et persister les données
        $entityManager = $doctrine->getManager();
        $entityManager->persist($book);
        $entityManager->flush(); // Sauvegarde en base de données

        // Redirection après le succès de l'ajout
        return $this->redirectToRoute('app_afficherbook');
    }

    // Rendu de la vue avec le formulaire
    return $this->renderForm('book/form.html.twig', [
        'form' => $form,
    ]);
}


#[Route('/affichebook', name: 'app_afficherbook')]
    public function afficherbook(BookRepository $repository): Response
    {
        $books= $repository->findAll();
        return $this->render('book/afficherbook.html.twig', [
       'books' => $books
        ]);
    }


  


    }