<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Attribute\Route;
use App\Repository\AuthorRepository;
use Doctrine\Persistence\ManagerRegistry;
use App\Form;
use App\Entity\Author;
use App\Form\AuthorType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType; 



class AuthorController extends AbstractController
{

    #[Route('/listAuthors', name: 'app_listAuthors')]
    public function index(): Response
    {
        $authors = array(
            array('id' => 1, 'picture' => '/images/Victor-Hugo.jpg.png','username' => 'Victor Hugo', 'email' =>
            'victor.hugo@gmail.com ', 'nb_books' => 100),
            array('id' => 2, 'picture' => '/images/william-shakespeare.jpg.png','username' => ' William Shakespeare', 'email' =>
            ' william.shakespeare@gmail.com', 'nb_books' => 200 ),
            array('id' => 3, 'picture' => '/images/Taha-Hussein.jpg.png','username' => 'Taha Hussein', 'email' =>
            'taha.hussein@gmail.com', 'nb_books' => 300),
            );

            return $this->render('author/author.index.html.twig', [
                'authors' => $authors, 
            ]);
        }
        /*#[Route('/details', name: 'app_details')]
    public function index1(): Response
    {
        return $this->render('author/details.html.twig', [
       
        ]);
    }*/
    #[Route('/details/{id}', name: 'app_details')]
public function showDetails($id): Response
{
    // Liste d'auteurs simulée
    $authors = array(
        array('id' => 1, 'picture' => '/images/Victor-Hugo.jpg.png','username' => 'Victor Hugo', 'email' => 'victor.hugo@gmail.com', 'nb_books' => 100),
        array('id' => 2, 'picture' => '/images/william-shakespeare.jpg.png','username' => 'William Shakespeare', 'email' => 'william.shakespeare@gmail.com', 'nb_books' => 200 ),
        array('id' => 3, 'picture' => '/images/Taha-Hussein.jpg.png','username' => 'Taha Hussein', 'email' => 'taha.hussein@gmail.com', 'nb_books' => 300),
    );

    // Recherche de l'auteur correspondant à l'ID
    $author = null;
    foreach ($authors as $a) {
        if ($a['id'] == $id) {
            $author = $a;
            break;
        }
    }

    // Si l'auteur n'est pas trouvé, afficher une erreur
    if ($author === null) {
        throw $this->createNotFoundException('Auteur non trouvé.');
    }

    // Renvoyer les détails de l'auteur à la vue
    return $this->render('author/details.html.twig', [
        'author' => $author
    ]);
}


 #[Route('/afficherauthor', name: 'app_afficherauthor')]
    public function afficherauthor(AuthorRepository $repository): Response
    {
        $author= $repository->findAll();
        return $this->render('author/afficherauthor.html.twig', [
       'authors' => $author
        ]);
    }

    #[Route('/ajout', name: 'app_ajout')]
public function ajout(Request $request, ManagerRegistry $doctrine): Response
{
    $author = new Author();
    $form = $this->createForm(AuthorType::class, $author);

    // Traiter la requête du formulaire
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        // Récupérer le gestionnaire d'entité et persister les données
        $entityManager = $doctrine->getManager();
        $entityManager->persist($author);
        $entityManager->flush(); // Sauvegarde en base de données

        // Redirection après le succès de l'ajout
        return $this->redirectToRoute('app_afficherauthor');
    }

    // Rendu de la vue avec le formulaire
    return $this->renderForm('author/form.html.twig', [
        'form' => $form,
    ]);
}

#[Route('/delete/{id}', name: 'app_delete')]
public function delete(Request $request, ManagerRegistry $doctrine, $id): Response
{
    $entityManager = $doctrine->getManager();
    $author = $entityManager->getRepository(Author::class)->find($id);

    if (!$author) {
        throw $this->createNotFoundException('Auteur non trouvé.');
    }

    // Supprimer l'auteur
    $entityManager->remove($author);
    $entityManager->flush(); // Appliquer les changements à la base de données

    // Rediriger vers la liste des auteurs après suppression
    return $this->redirectToRoute('app_afficherauthor');
}



#[Route('/edit/{id}', name: 'app_edit')]
public function edit(Request $request, ManagerRegistry $doctrine, $id): Response
{
   
    $entityManager = $doctrine->getManager();
    $author = $entityManager->getRepository(Author::class)->find($id);

    if (!$author) {
        throw $this->createNotFoundException('Auteur non trouvé.');
    }

 
    $form = $this->createForm(AuthorType::class, $author);

 
    $form->handleRequest($request);

 
    if ($form->isSubmitted() && $form->isValid()) {
       
        $entityManager->flush(); // Enregistrer les modifications dans la base de données

        // Redirection après le succès de la modification
        return $this->redirectToRoute('app_afficherauthor');
    }

    // Rendu du formulaire avec les données de l'auteur préremplies
    return $this->renderForm('author/form.html.twig', [
        'form' => $form,
    ]);

}

#[Route('/chercherauthor', name: 'app_chercherauthor')]
public function chercherauthor(AuthorRepository $repository): Response
{
    $authors= $repository->chercherauthor();
    return $this->render('author/afficherauthor.html.twig', [
   'authors' => $authors
    ]);
}
 
#[Route('/chercherauthordql', name: 'app_chercherauthordql')]
public function chercherauthordql(AuthorRepository $repository): Response
{
    $authors= $repository->chercherauthordql("ESPRIT");
    return $this->render('author/afficherauthor.html.twig', [
   'authors' => $authors
    ]);
}


#[Route('/sup20', name: 'app_sup20')]
public function sup20(AuthorRepository $repository): Response
{
    $authors= $repository->findAuthorsWithMoreThanTwentyBooks();
    return $this->render('author/afficherauthor.html.twig', [
   'authors' => $authors
    ]);
}

    }