<?php

namespace App\Entity;

use App\Repository\PostRepository;
use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\EntityManagerInterface;

#[ORM\Entity(repositoryClass: PostRepository::class)]
class Post
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column(type: 'integer')]
    private ?int $id = null;

    #[ORM\Column(type: 'date')]
    private ?\DateTimeInterface $date = null;

    #[ORM\Column(type: 'string', length: 250)]
    private ?string $author = null;

    #[ORM\Column(type: 'string', length: 1000)]
    private ?string $contenu = null;

    #[ORM\Column(type: 'integer', options: ['default' => 0])]
private int $upvotes = 0;

#[ORM\Column(type: 'integer', options: ['default' => 0])]
private int $downvotes = 0;


#[ORM\Column(type: 'string', length: 1000, nullable: true)]
private ?string $image = null;

    #[ORM\ManyToOne(targetEntity: Client::class)]
    #[ORM\JoinColumn(name: 'client_id', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private ?Client $client = null;


    #[ORM\Column(name: 'client_id', type: 'integer')]
    private ?int $clientId = null;


    #[ORM\OneToMany(mappedBy: "post", targetEntity: Commentaire::class, cascade: ["remove"])]
    private Collection $commentaires;


    #[ORM\OneToMany(mappedBy: 'post', targetEntity: Vote::class, cascade: ['persist', 'remove'])]
private Collection $votes;

    


   

    public function getCommentaires(): Collection
    {
    return $this->commentaires;
    }


    public function getId(): ?int
    {
        return $this->id;
    }

    public function getDate(): ?\DateTimeInterface
    {
        return $this->date;
    }

    public function setDate(\DateTimeInterface $date): static
    {
        $this->date = $date;

        return $this;
    }

    public function getAuthor(): ?string
    {
        return $this->author;
    }

    public function setAuthor(string $author): static
    {
        $this->author = $author;

        return $this;
    }

    public function getContenu(): ?string
    {
        return $this->contenu;
    }

    public function setContenu(string $contenu): static
    {
        $this->contenu = $contenu;

        return $this;
    }

    public function getUpvotes(): int
{
    return $this->upvotes;
}

public function setUpvotes(int $upvotes): self
{
    $this->upvotes = $upvotes;
    return $this;
}

public function getDownvotes(): int
{
    return $this->downvotes;
}

public function setDownvotes(int $downvotes): self
{
    $this->downvotes = $downvotes;
    return $this;
}


    public function getImage(): ?string
    {
        return $this->image;
    }

    

    public function setImage(?string $image): static
{
    $this->image = $image;
    return $this;
}

    public function getClient(): ?Client
    {
        return $this->client;
    }

    public function setClient(?Client $client): void
    {
        $this->client = $client;
    }

public function getClientId(): ?int
{
    return $this->clientId;
}

public function setClientId(int $clientId): self
{
    $this->clientId = $clientId;
    return $this;
}




public function incrementUpvote(): void
{
    $this->upvotes++;
}

public function decrementUpvote(): void
{
    if ($this->upvotes > 0) {
        $this->upvotes--;
    }
}

public function incrementDownvote(): void
{
    $this->downvotes++;
}

public function decrementDownvote(): void
{
    if ($this->downvotes > 0) {
        $this->downvotes--;
    }
}


    public function getVotes(): Collection
{
    return $this->votes;
}





public function __construct()
{
    $this->commentaires = new ArrayCollection();
    $this->date = new \DateTime();  
    $this->votes = new ArrayCollection();  // Date actuelle par défaut
         
   
    
}


   




    
}
