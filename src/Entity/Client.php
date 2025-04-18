<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\ClientRepository;

#[ORM\Entity(repositoryClass: ClientRepository::class)]
#[ORM\Table(name: 'client')]
class Client 
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function setId(int $id): self
    {
        $this->id = $id;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $nom = null;

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): self
    {
        $this->nom = $nom;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $prenom = null;

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): self
    {
        $this->prenom = $prenom;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $sexe = null;

    public function getSexe(): ?string
    {
        return $this->sexe;
    }

    public function setSexe(string $sexe): self
    {
        $this->sexe = $sexe;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $mdp = null;

    public function getMdp(): ?string
    {
        return $this->mdp;
    }

    public function setMdp(string $mdp): self
    {
        $this->mdp = $mdp;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $dateNaissance = null;

    public function getDateNaissance(): ?string
    {
        return $this->dateNaissance;
    }

    public function setDateNaissance(string $dateNaissance): self
    {
        $this->dateNaissance = $dateNaissance;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $email = null;

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): self
    {
        $this->email = $email;
        return $this;
    }

    #[ORM\Column(type: 'float', nullable: false)]
    private ?float $poids = null;

    public function getPoids(): ?float
    {
        return $this->poids;
    }

    public function setPoids(float $poids): self
    {
        $this->poids = $poids;
        return $this;
    }

    #[ORM\Column(type: 'decimal', nullable: false)]
    private ?float $taille = null;

    public function getTaille(): ?float
    {
        return $this->taille;
    }

    public function setTaille(float $taille): self
    {
        $this->taille = $taille;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $Objectif = null;

    public function getObjectif(): ?string
    {
        return $this->Objectif;
    }

    public function setObjectif(string $Objectif): self
    {
        $this->Objectif = $Objectif;
        return $this;
    }

    #[ORM\ManyToOne(targetEntity: Event::class, inversedBy: 'clients')]
    #[ORM\JoinColumn(name: 'idEvent', referencedColumnName: 'id')]
    private ?Event $event = null;

    public function getEvent(): ?Event
    {
        return $this->event;
    }

    public function setEvent(?Event $event): self
    {
        $this->event = $event;
        return $this;
    }

    #[ORM\ManyToOne(targetEntity: Programme::class, inversedBy: 'clients')]
    #[ORM\JoinColumn(name: 'id_prog', referencedColumnName: 'id')]
    private ?Programme $programme = null;

    public function getProgramme(): ?Programme
    {
        return $this->programme;
    }

    public function setProgramme(?Programme $programme): self
    {
        $this->programme = $programme;
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Commande::class, mappedBy: 'client')]
    private Collection $commandes;

    /**
     * @return Collection<int, Commande>
     */
    public function getCommandes(): Collection
    {
        if (!$this->commandes instanceof Collection) {
            $this->commandes = new ArrayCollection();
        }
        return $this->commandes;
    }

    public function addCommande(Commande $commande): self
    {
        if (!$this->getCommandes()->contains($commande)) {
            $this->getCommandes()->add($commande);
        }
        return $this;
    }

    public function removeCommande(Commande $commande): self
    {
        $this->getCommandes()->removeElement($commande);
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Commentaire::class, mappedBy: 'client')]
    private Collection $commentaires;

    /**
     * @return Collection<int, Commentaire>
     */
    public function getCommentaires(): Collection
    {
        if (!$this->commentaires instanceof Collection) {
            $this->commentaires = new ArrayCollection();
        }
        return $this->commentaires;
    }

    public function addCommentaire(Commentaire $commentaire): self
    {
        if (!$this->getCommentaires()->contains($commentaire)) {
            $this->getCommentaires()->add($commentaire);
        }
        return $this;
    }

    public function removeCommentaire(Commentaire $commentaire): self
    {
        $this->getCommentaires()->removeElement($commentaire);
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Post::class, mappedBy: 'client')]
    private Collection $posts;

    public function __construct()
    {
        $this->commandes = new ArrayCollection();
        $this->commentaires = new ArrayCollection();
        $this->posts = new ArrayCollection();
    }

    /**
     * @return Collection<int, Post>
     */
    public function getPosts(): Collection
    {
        if (!$this->posts instanceof Collection) {
            $this->posts = new ArrayCollection();
        }
        return $this->posts;
    }

    public function addPost(Post $post): self
    {
        if (!$this->getPosts()->contains($post)) {
            $this->getPosts()->add($post);
        }
        return $this;
    }

    public function removePost(Post $post): self
    {
        $this->getPosts()->removeElement($post);
        return $this;
    }

}
