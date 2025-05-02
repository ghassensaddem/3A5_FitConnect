<?php

namespace App\Controller;
// Correct
use App\Form\CommentaireType;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\PostRepository;
use App\Repository\ClientRepository;
use App\Entity\Post;
use App\Entity\Client;
use App\Entity\Vote;
use Doctrine\Persistence\ManagerRegistry;
use App\Repository\CommentaireRepository;
use App\Entity\Commentaire;
use App\Form\PostType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\JsonResponse;






class PostController extends AbstractController

{


    
    
    #[Route('/listPosts', name: 'app_listPosts')]
public function index(PostRepository $repository): Response
{
    $posts = $repository->findAll();

    return $this->render('post/index.html.twig', [
        'posts' => $posts,
    ]);
}





#[Route('/listPosts_front', name: 'app_listPosts_front')]
public function index_front(PostRepository $repository): Response
{
    $posts = $repository->findAll();
    $staticClientId = 1;

    return $this->render('post/forum.html.twig', [
        'posts' => $posts,
        'client_id' => $staticClientId,
    ]);
}




#[Route('/post/add', name: 'app_post_add')]
public function ajouter(Request $request, EntityManagerInterface $em): Response
{
    // Créer une nouvelle instance de Post
    $post = new Post();
    $client = $em->find(Client::class, 1);
    $post->setClient($client);
    $post->setAuthor($client->getNom());

    // Créer le formulaire
    $form = $this->createForm(PostType::class, $post);
    $form->handleRequest($request);

    // Vérifier si le formulaire a été soumis et est valide
    if ($form->isSubmitted() && $form->isValid()) {
        // Récupérer le fichier de l'image
        $imageFile = $form->get('imageFile')->getData();

        // Vérifier si un fichier a été téléchargé
        if ($imageFile) {
            // Générer un nom unique pour l'image
            $newFilename = uniqid().'.'.$imageFile->guessExtension();
            $imageFile->move(
                $this->getParameter('photo_dir'),
                $newFilename
            );
           
            $post->setImage($newFilename);
        }

        // Persister l'entité Post
        $em->persist($post);
        $em->flush();

        // Rediriger vers la liste des posts après l'ajout
        return $this->redirectToRoute('app_listPosts_front');
    }

    // Rendre le formulaire dans la vue
    return $this->render('post/ajouter_post.html.twig', [
        'form' => $form->createView(),
    ]);
}





#[Route('/comment/add/{id}', name: 'app_comment_add')]
public function ajouter_com(int $id, Request $request, EntityManagerInterface $em): Response {
    // Récupérer le Post en question
    $post = $em->getRepository(Post::class)->find($id);

    if (!$post) {
        throw $this->createNotFoundException("Post non trouvé !");
    }

    // Créer une nouvelle instance de Commentaire
    $commentaire = new Commentaire();

    // Récupérer le client (ici en dur, mais idéalement depuis l'utilisateur connecté)
    $client = $em->find(Client::class, 1);

    // Associer le commentaire au client et au post
    $commentaire->setClient($client);
    $commentaire->setPost($post);
    $commentaire->setAuthor($client->getNom()); // ou getNomComplet(), ou un champ "username"

    // Créer le formulaire
    $form = $this->createForm(CommentaireType::class, $commentaire);
    $form->handleRequest($request);

    // Vérifier si le formulaire a été soumis et est valide
    if ($form->isSubmitted() && $form->isValid()) {
        // Persister et flush
        $em->persist($commentaire);
        $em->flush();

        // Rediriger (par exemple vers les commentaires du post)
        return $this->redirectToRoute('app_post_comments_front', ['id' => $post->getId()]);
    }

    // Récupérer les commentaires existants pour les afficher dans la vue
    $comments = $em->getRepository(Commentaire::class)->findBy(['post' => $post]);

    // Rendre le formulaire et les commentaires dans la vue
    return $this->render('commentaire/afficher_com.html.twig', [
        'form' => $form->createView(),
        'comments' => $comments,
        'post' => $post,
    ]);
}


#[Route('/delete/{id}', name: 'app_delete')]
public function delete(Request $request, ManagerRegistry $doctrine, $id): Response
{
    $entityManager = $doctrine->getManager();
    $post = $entityManager->getRepository(Post::class)->find($id);

    if (!$post) {
        throw $this->createNotFoundException('Post non trouvé.');
    }

    // Supprimer le post
    $entityManager->remove($post);
    $entityManager->flush(); // Appliquer les changements à la base de données

    // Rediriger vers la liste des posts après suppression
    return $this->redirectToRoute('app_listPosts');
}


#[Route('/delete/{id}/front', name: 'app_delete_front')]
public function delete_front(Request $request, ManagerRegistry $doctrine, $id): Response
{
    $entityManager = $doctrine->getManager();
    $post = $entityManager->getRepository(Post::class)->find($id);

    if (!$post) {
        throw $this->createNotFoundException('Post non trouvé.');
    }

    // Supprimer le post
    $entityManager->remove($post);
    $entityManager->flush(); // Appliquer les changements à la base de données

    // Rediriger vers la liste des posts après suppression
    return $this->redirectToRoute('app_listPosts_front');
}





#[Route('/post/{id}/comments', name: 'app_post_comments')]
public function showComments(PostRepository $postRepository, CommentaireRepository $commentaireRepository, $id): Response
{
    // Récupérer le post
    $post = $postRepository->find($id);

    if (!$post) {
        throw $this->createNotFoundException('Post non trouvé.');
    }

    // Récupérer les commentaires liés à ce post
    $comments = $commentaireRepository->findBy(['post' => $post]);

    return $this->render('commentaire/index_com.html.twig', [
        'post' => $post,
        'comments' => $comments,
    ]);
}

#[Route('/post/{id}/comments/front', name: 'app_post_comments_front')]
public function showComments_front(PostRepository $postRepository, CommentaireRepository $commentaireRepository, Request $request,EntityManagerInterface $em, int $id): Response {
    // Récupérer le Post en question
    $post = $postRepository->find($id);

    if (!$post) {
        throw $this->createNotFoundException('Post non trouvé.');
    }

    // Récupérer les commentaires liés à ce post
    $comments = $commentaireRepository->findBy(['post' => $post]);

    // Créer une instance de Commentaire pour le formulaire
    $commentaire = new Commentaire();
    $client = $em->find(Client::class, 1);  // À remplacer par l'utilisateur connecté
    $commentaire->setClient($client);
    $commentaire->setPost($post);
    $commentaire->setAuthor($client->getNom());  // ou $client->getNomComplet() selon la logique

    // Créer le formulaire pour ajouter un commentaire
    $form = $this->createForm(CommentaireType::class, $commentaire);
    $form->handleRequest($request);

    // Vérifier si le formulaire a été soumis et est valide
    if ($form->isSubmitted() && $form->isValid()) {
        // Persister l'entité Commentaire
        $em->persist($commentaire);
        $em->flush();

        // Rediriger vers la même page pour afficher les commentaires mis à jour
        return $this->redirectToRoute('app_post_comments_front', ['id' => $post->getId()]);
    }





    // Rendre la vue avec les commentaires et le formulaire
    return $this->render('commentaire/afficher_com.html.twig', [
        'post' => $post,
        'comments' => $comments,
        'form' => $form->createView(),  
        'client' => $client,
    ]);
}



#[Route('/post/edit/{id}', name: 'app_post_edit')]
    public function edit(int $id, Request $request, EntityManagerInterface $em): Response
    {
        // Récupérer le post à modifier
        $post = $em->getRepository(Post::class)->find($id);

        if (!$post) {
            throw $this->createNotFoundException('Le post avec l\'id ' . $id . ' n\'existe pas.');
        }

        $oldImage =$post->getImage();
        $form = $this->createForm(PostType::class, $post);

        // Gérer la soumission du formulaire
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Gérer l'image si elle a été modifiée
            /** @var UploadedFile $imageFile */
            $imageFile = $form->get('imageFile')->getData();

             // Vérifier si un fichier a été téléchargé
             if ($imageFile) {
                if ($oldImage) {
                    $oldImagePath = $this->getParameter('photo_dir').'/'.$oldImage;
                    if (file_exists($oldImagePath)) {
                        unlink($oldImagePath);
                    }
                }
                
                $newFilename = uniqid().'.'.$imageFile->guessExtension();
                $imageFile->move(
                    $this->getParameter('photo_dir'),
                    $newFilename
                );
                $post->setImage($newFilename);
            }


            // Enregistrer les modifications
            $em->flush();

            // Rediriger vers la liste des posts ou une autre page
            return $this->redirectToRoute('app_listPosts_front');
        }

        // Rendre le formulaire dans la vue
        return $this->render('post/modifier_post.html.twig', [
            'form' => $form->createView(),
            'post' => $post,  // Passer la variable 'post' au template
        ]);
    }




    #[Route('/post/{id}/upvote', name: 'app_post_upvote')]
public function upvote(int $id, EntityManagerInterface $em): JsonResponse
{
    // Récupérer le post
    $post = $em->getRepository(Post::class)->find($id);

    if (!$post) {
        throw $this->createNotFoundException('Post non trouvé.');
    }

    // Incrémenter le nombre d'upvotes
    $post->setUpvotes($post->getUpvotes() + 1);

    // Sauvegarder les changements dans la base de données
    $em->persist($post);
    $em->flush();

    // Retourner les nouveaux upvotes et downvotes en JSON
    return new JsonResponse([
        'upvotes' => $post->getUpvotes(),
        'downvotes' => $post->getDownvotes(),
    ]);
}

#[Route('/post/{id}/downvote', name: 'app_post_downvote')]
public function downvote(int $id, EntityManagerInterface $em): JsonResponse
{
    // Récupérer le post
    $post = $em->getRepository(Post::class)->find($id);

    if (!$post) {
        throw $this->createNotFoundException('Post non trouvé.');
    }

    // Incrémenter le nombre de downvotes
    $post->setDownvotes($post->getDownvotes() + 1);

    // Sauvegarder les changements dans la base de données
    $em->persist($post);
    $em->flush();

    // Retourner les nouveaux upvotes et downvotes en JSON
    return new JsonResponse([
        'upvotes' => $post->getUpvotes(),
        'downvotes' => $post->getDownvotes(),
    ]);
}



   

    private function applyVote(Post $post, int $type, int $delta): void
    {
        if ($type === 1) {
            $post->setUpvotes($post->getUpvotes() + $delta);
        } elseif ($type === -1) {
            $post->setDownvotes($post->getDownvotes() + $delta);
        }
    }



    #[Route('/post/{id}/vote/{type}', name: 'app_post_vote')]
public function vote(
    int $id, 
    int $type, 
    EntityManagerInterface $em, 
    Request $request
): Response {
    // Validation du type de vote
    if (!in_array($type, [1, -1], true)) {
        throw $this->createNotFoundException('Type de vote invalide.');
    }

    // Récupération du post
    $post = $em->getRepository(Post::class)->find($id);
    if (!$post) {
        throw $this->createNotFoundException('Post non trouvé.');
    }

    // Récupération du client avec l'ID 8
    $client = $em->getRepository(Client::class)->find(1);
    if (!$client) {
        throw $this->createNotFoundException('Client non trouvé.');
    }

    // Gestion de la transaction
    $em->beginTransaction();
    try {
        // Recherche d'un vote existant pour ce post et ce client
        $voteRepo = $em->getRepository(Vote::class);
        $existingVote = $voteRepo->findOneBy([
            'post' => $post,
            'client' => $client
        ]);

        // Si un vote existe déjà pour ce client et ce post
        if ($existingVote) {
            $previousVoteType = $existingVote->getVoteType();

            if ($previousVoteType === $type) {
                // Annulation du vote
                if ($type == 1) {
                    $post->decrementUpvote();
                } elseif ($type == -1) {
                    $post->decrementDownvote();
                }
                $em->remove($existingVote);
            } else {
                // Changement de vote
                if ($type == 1) {
                    $post->incrementUpvote();
                    $post->decrementDownvote();
                } elseif ($type == -1) {
                    $post->incrementDownvote();
                    $post->decrementUpvote();
                }
                $existingVote->setVoteType($type);
            }
        } else {
         
            $vote = new Vote();
            $vote->setPost($post);
            $vote->setClient($client);
            $vote->setVoteType($type);

            if ($type == 1) {
                $post->incrementUpvote();
            } elseif ($type == -1) {
                $post->incrementDownvote();
            }

            $em->persist($vote);
        }

        // Force la mise à jour de l'entité Post
        $em->persist($post);
        $em->flush();
        $em->commit();

        // Réponse Ajax pour mettre à jour les compteurs de votes
        if ($request->isXmlHttpRequest()) {
            return new JsonResponse([
                'upvotes' => $post->getUpvotes(),
                'downvotes' => $post->getDownvotes(),
            ]);
        }

        return $this->redirectToRoute('app_listPosts_front');
        
    } catch (\Exception $e) {
        $em->rollback();
        throw $e;
    }
}
#[Route('/post/{id}/vote_post/{type}', name: 'app_vote_post', methods: ['POST'])]
public function vote_post(
    int $id,
    int $type,
    EntityManagerInterface $em,
    Request $request
): JsonResponse {
    if (!in_array($type, [1, -1], true)) {
        return new JsonResponse(['error' => 'Type de vote invalide.'], 400);
    }

    $post = $em->getRepository(Post::class)->find($id);
    if (!$post) {
        return new JsonResponse(['error' => 'Post non trouvé.'], 404);
    }

    // Récupération du client statique (à remplacer plus tard par le connecté)
    $client = $em->getRepository(Client::class)->find(1);
    if (!$client) {
        return new JsonResponse(['error' => 'Client non trouvé.'], 404);
    }

    $voteRepo = $em->getRepository(Vote::class);
    $existingVote = $voteRepo->findOneBy([
        'post' => $post,
        'client' => $client,
    ]);

    if ($existingVote) {
        $previousVote = $existingVote->getVoteType();

        if ($previousVote === $type) {
            // 👇 Même vote => annulation
            if ($type === 1) {
                $post->decrementUpvote();
            } else {
                $post->decrementDownvote();
            }

            $em->remove($existingVote);
        } else {
            // 🔁 Changement de vote
            if ($type === 1) {
                $post->incrementUpvote();
                $post->decrementDownvote();
            } else {
                $post->incrementDownvote();
                $post->decrementUpvote();
            }

            $existingVote->setVoteType($type);
            $em->persist($existingVote);
        }
    } else {
        // 🆕 Nouveau vote
        $vote = new Vote();
        $vote->setPost($post);
        $vote->setClient($client);
        $vote->setVoteType($type);

        if ($type === 1) {
            $post->incrementUpvote();
        } else {
            $post->incrementDownvote();
        }

        $em->persist($vote);
    }

    $em->persist($post);
    $em->flush();

    return new JsonResponse([
        'upvotes' => $post->getUpvotes(),
        'downvotes' => $post->getDownvotes()
    ]);
}








    
    /*// Afficher les détails d'un post
    #[Route('/detailsPost/{id}', name: 'app_detailsPost')]
    public function showDetails($id, PostRepository $repository): Response
    {
        // Récupérer un post par son ID
        $post = $repository->find($id);

        // Si le post n'existe pas, afficher une erreur
        if (!$post) {
            throw $this->createNotFoundException('Post non trouvé.');
        }

        return $this->render('post/detailsPost.html.twig', [
            'post' => $post
        ]);
    }*/


    
}
