<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Security\Core\User\UserInterface;

use App\Entity\Event;
use Doctrine\Common\Collections\Collection;
use App\Entity\Ratingactivity;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
#[ORM\Entity]
class Client  implements UserInterface , PasswordAuthenticatedUserInterface
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

    #[ORM\Column(type: "string", length: 50)]
    #[Assert\NotBlank(message: "Le nom ne peut pas être vide.")]
    #[Assert\Length(
        min: 2,
        max: 50,
        minMessage: "Le nom doit avoir au moins {{ limit }} caractères.",
        maxMessage: "Le nom ne peut pas dépasser {{ limit }} caractères."
    )]
    private $nom;

    #[ORM\Column(type: "string", length: 50)]
    #[Assert\NotBlank(message: "Le prénom ne peut pas être vide.")]
    #[Assert\Length(
        min: 2,
        max: 50,
        minMessage: "Le prénom doit avoir au moins {{ limit }} caractères.",
        maxMessage: "Le prénom ne peut pas dépasser {{ limit }} caractères."
    )]
    private  $prenom;

    #[ORM\Column(type: "string", length: 10)]
    #[Assert\NotBlank(message: "Le sexe ne peut pas être vide.")]
    #[Assert\Choice(choices: ["Homme", "Femme"], message: "Choisissez un sexe valide.")]
    private string $sexe;

    #[ORM\Column(type: "string", length: 250)]
    #[Assert\NotBlank(message: "Le mot de passe ne peut pas être vide.")]
    #[Assert\Length(
        min: 8,
        minMessage: "Le mot de passe doit comporter au moins {{ limit }} caractères."
    )]
    private  $mdp;

    #[ORM\Column(type: "string", name: "dateNaissance")]
    private  $dateNaissance;

    #[ORM\Column(type: "string", length: 100)]
    #[Assert\NotBlank(message: "L'email ne peut pas être vide.")]
    #[Assert\Email(message: "L'email doit être valide.")]
    private  $email;

    #[ORM\Column(type: "float")]
    #[Assert\NotBlank(message: "Le poids ne peut pas être vide.")]
    #[Assert\Type(type: "numeric", message: "Le poids doit être un nombre.")]
    #[Assert\Range(
        min: 30,
        max: 200,
        notInRangeMessage: "Le poids doit être compris entre {{ min }} et {{ max }} kg."
    )]
    private  $poids;

    #[ORM\Column(type: "float")]
    #[Assert\NotBlank(message: "La taille ne peut pas être vide.")]
    #[Assert\Type(type: "numeric", message: "La taille doit être un nombre.")]
    #[Assert\Range(
        min: 100,
        max: 250,
        notInRangeMessage: "La taille doit être comprise entre {{ min }} et {{ max }} cm."
    )]
    private  $taille;


    #[ORM\Column(type: "string", length: 255)]
    #[Assert\Image(
        mimeTypes: ["image/jpeg", "image/png"],
        mimeTypesMessage: "L'image doit être au format JPG ou PNG.",
        maxSize: "5M",
        maxSizeMessage: "L'image ne doit pas dépasser 5 Mo."
    )]
    private  $image;


        #[ORM\ManyToOne(targetEntity: Event::class, inversedBy: "clients")]
    #[ORM\JoinColumn(name: 'idEvent', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Event $idEvent;

    #[ORM\Column(type: "integer")]
    private int $id_prog;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getNom()
    {
        return $this->nom;
    }

    public function setNom($value)
    {
        $this->nom = $value;
    }

    public function getPrenom()
    {
        return $this->prenom;
    }

    public function setPrenom($value)
    {
        $this->prenom = $value;
    }

    public function getSexe()
    {
        return $this->sexe;
    }

    public function setSexe($value)
    {
        $this->sexe = $value;
    }

    public function getMdp()
    {
        return $this->mdp;
    }

    public function setMdp($value)
    {
        $this->mdp = $value;
    }

    public function getDateNaissance()
    {
        return $this->dateNaissance;
    }

    public function setDateNaissance($value)
    {
        $this->dateNaissance = $value;
    }

    public function getEmail()
    {
        return $this->email;
    }

    public function setEmail($value)
    {
        $this->email = $value;
    }

    public function getPoids()
    {
        return $this->poids;
    }

    public function setPoids($value)
    {
        $this->poids = $value;
    }

    public function getTaille()
    {
        return $this->taille;
    }

    public function setTaille($value)
    {
        $this->taille = $value;
    }

    public function getImage()
    {
        return $this->image;
    }

    public function setImage($value)
    {
        $this->image = $value;
    }

    public function getIdEvent()
    {
        return $this->idEvent;
    }

    public function setIdEvent($value)
    {
        $this->idEvent = $value;
    }

    public function getId_prog()
    {
        return $this->id_prog;
    }

    public function setId_prog($value)
    {
        $this->id_prog = $value;
    }

    #[ORM\OneToMany(mappedBy: "ClientId", targetEntity: Poids::class)]
    private Collection $poidss;

        public function getPoidss(): Collection
        {
            return $this->poidss;
        }
    
        public function addPoids(Poids $poids): self
        {
            if (!$this->poidss->contains($poids)) {
                $this->poidss[] = $poids;
                $poids->setClientId($this);
            }
    
            return $this;
        }
    
        public function removePoids(Poids $poids): self
        {
            if ($this->poidss->removeElement($poids)) {
                // set the owning side to null (unless already changed)
                if ($poids->getClientId() === $this) {
                    $poids->setClientId(null);
                }
            }
    
            return $this;
        }

    #[ORM\OneToMany(mappedBy: "client_id", targetEntity: Post::class)]
    private Collection $posts;

        public function getPosts(): Collection
        {
            return $this->posts;
        }
    
        public function addPost(Post $post): self
        {
            if (!$this->posts->contains($post)) {
                $this->posts[] = $post;
                $post->setClient_id($this);
            }
    
            return $this;
        }
    
        public function removePost(Post $post): self
        {
            if ($this->posts->removeElement($post)) {
                // set the owning side to null (unless already changed)
                if ($post->getClient_id() === $this) {
                    $post->setClient_id(null);
                }
            }
    
            return $this;
        }

    #[ORM\OneToMany(mappedBy: "idClient", targetEntity: Application::class)]
    private Collection $applications;

        public function getApplications(): Collection
        {
            return $this->applications;
        }
    
        public function addApplication(Application $application): self
        {
            if (!$this->applications->contains($application)) {
                $this->applications[] = $application;
                $application->setIdClient($this);
            }
    
            return $this;
        }
    
        public function removeApplication(Application $application): self
        {
            if ($this->applications->removeElement($application)) {
                // set the owning side to null (unless already changed)
                if ($application->getIdClient() === $this) {
                    $application->setIdClient(null);
                }
            }
    
            return $this;
        }

    #[ORM\OneToMany(mappedBy: "clientId", targetEntity: Commande::class)]
    private Collection $commandes;

        public function getCommandes(): Collection
        {
            return $this->commandes;
        }
    
        public function addCommande(Commande $commande): self
        {
            if (!$this->commandes->contains($commande)) {
                $this->commandes[] = $commande;
                $commande->setClientId($this);
            }
    
            return $this;
        }
    
        public function removeCommande(Commande $commande): self
        {
            if ($this->commandes->removeElement($commande)) {
                // set the owning side to null (unless already changed)
                if ($commande->getClientId() === $this) {
                    $commande->setClientId(null);
                }
            }
    
            return $this;
        }

    #[ORM\OneToMany(mappedBy: "client_id", targetEntity: Vote::class)]
    private Collection $votes;

    #[ORM\OneToMany(mappedBy: "client_id", targetEntity: Vote_com::class)]
    private Collection $vote_coms;

    #[ORM\OneToMany(mappedBy: "client_id", targetEntity: Commentaire::class)]
    private Collection $commentaires;

    #[ORM\OneToMany(mappedBy: "idClient", targetEntity: Ratingactivity::class)]
    private Collection $ratingactivitys;

    public function __construct()
    {
        $this->poidss = new ArrayCollection();
        $this->posts = new ArrayCollection();
        $this->applications = new ArrayCollection();
        $this->commandes = new ArrayCollection();
        $this->votes = new ArrayCollection();
        $this->vote_coms = new ArrayCollection();
        $this->commentaires = new ArrayCollection();
        $this->ratingactivitys = new ArrayCollection();
    }

    public function getIdProg(): ?int
    {
        return $this->id_prog;
    }

    public function setIdProg(int $id_prog): static
    {
        $this->id_prog = $id_prog;

        return $this;
    }

    public function addPoidss(Poids $poidss): static
    {
        if (!$this->poidss->contains($poidss)) {
            $this->poidss->add($poidss);
            $poidss->setClientId($this);
        }

        return $this;
    }

    public function removePoidss(Poids $poidss): static
    {
        if ($this->poidss->removeElement($poidss)) {
            // set the owning side to null (unless already changed)
            if ($poidss->getClientId() === $this) {
                $poidss->setClientId(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, Vote>
     */
    public function getVotes(): Collection
    {
        return $this->votes;
    }

    public function addVote(Vote $vote): static
    {
        if (!$this->votes->contains($vote)) {
            $this->votes->add($vote);
            $vote->setClientId($this);
        }

        return $this;
    }

    public function removeVote(Vote $vote): static
    {
        if ($this->votes->removeElement($vote)) {
            // set the owning side to null (unless already changed)
            if ($vote->getClientId() === $this) {
                $vote->setClientId(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, Vote_com>
     */
    public function getVoteComs(): Collection
    {
        return $this->vote_coms;
    }

    public function addVoteCom(Vote_com $voteCom): static
    {
        if (!$this->vote_coms->contains($voteCom)) {
            $this->vote_coms->add($voteCom);
            $voteCom->setClientId($this);
        }

        return $this;
    }

    public function removeVoteCom(Vote_com $voteCom): static
    {
        if ($this->vote_coms->removeElement($voteCom)) {
            // set the owning side to null (unless already changed)
            if ($voteCom->getClientId() === $this) {
                $voteCom->setClientId(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, Commentaire>
     */
    public function getCommentaires(): Collection
    {
        return $this->commentaires;
    }

    public function addCommentaire(Commentaire $commentaire): static
    {
        if (!$this->commentaires->contains($commentaire)) {
            $this->commentaires->add($commentaire);
            $commentaire->setClientId($this);
        }

        return $this;
    }

    public function removeCommentaire(Commentaire $commentaire): static
    {
        if ($this->commentaires->removeElement($commentaire)) {
            // set the owning side to null (unless already changed)
            if ($commentaire->getClientId() === $this) {
                $commentaire->setClientId(null);
            }
        }

        return $this;
    }

    /**
     * @return Collection<int, Ratingactivity>
     */
    public function getRatingactivitys(): Collection
    {
        return $this->ratingactivitys;
    }

    public function addRatingactivity(Ratingactivity $ratingactivity): static
    {
        if (!$this->ratingactivitys->contains($ratingactivity)) {
            $this->ratingactivitys->add($ratingactivity);
            $ratingactivity->setIdClient($this);
        }

        return $this;
    }

    public function removeRatingactivity(Ratingactivity $ratingactivity): static
    {
        if ($this->ratingactivitys->removeElement($ratingactivity)) {
            // set the owning side to null (unless already changed)
            if ($ratingactivity->getIdClient() === $this) {
                $ratingactivity->setIdClient(null);
            }
        }

        return $this;
    }



    public function getUserIdentifier(): string
    {
        return $this->email;
    }


     /**
     * @return string|null
     */
    public function getPassword(): ?string
    {
        return $this->mdp;
    }

    /**
     * @param string $password
     * @return $this
     */
    public function setPassword(string $password): self
    {
        $this->mdp = $password;
        return $this;
    }

    /**
     * Méthode pour retourner le nom d'utilisateur (email ici)
     */
    public function getUsername(): string
    {
        return $this->email; // Ou le champ que tu utilises pour l'authentification
    }

    /**
     * Retourne un tableau des rôles de l'utilisateur
     */
    public function getRoles(): array
    {
        return ['ROLE_USER']; // Par exemple, tu peux avoir des rôles comme ROLE_ADMIN, ROLE_USER, etc.
    }

    /**
     * Retourne un tableau des informations supplémentaires
     */
    public function getSalt(): ?string
    {
        return null; // Par défaut, Symfony n'a pas besoin d'un "salt" si tu utilises un algorithme de hachage moderne comme bcrypt.
    }

    public function eraseCredentials()
    {
        // Efface les informations sensibles comme les mots de passe en clair
    }
}
