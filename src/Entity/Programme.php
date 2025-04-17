<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\ProgrammeRepository;

#[ORM\Entity(repositoryClass: ProgrammeRepository::class)]
#[ORM\Table(name: 'programme')]
class Programme
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

    #[ORM\Column(type: 'date', nullable: false)]
    private ?\DateTimeInterface $datedebut = null;

    public function getDatedebut(): ?\DateTimeInterface
    {
        return $this->datedebut;
    }

    public function setDatedebut(\DateTimeInterface $datedebut): self
    {
        $this->datedebut = $datedebut;
        return $this;
    }

    #[ORM\Column(type: 'date', nullable: false)]
    private ?\DateTimeInterface $datefin = null;

    public function getDatefin(): ?\DateTimeInterface
    {
        return $this->datefin;
    }

    public function setDatefin(\DateTimeInterface $datefin): self
    {
        $this->datefin = $datefin;
        return $this;
    }

    #[ORM\Column(type: 'float', nullable: false)]
    private ?float $prix = null;

    public function getPrix(): ?float
    {
        return $this->prix;
    }

    public function setPrix(float $prix): self
    {
        $this->prix = $prix;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $description = null;

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): self
    {
        $this->description = $description;
        return $this;
    }

    #[ORM\ManyToOne(targetEntity: Coach::class, inversedBy: 'programmes')]
    #[ORM\JoinColumn(name: 'coach_id', referencedColumnName: 'id')]
    private ?Coach $coach = null;

    public function getCoach(): ?Coach
    {
        return $this->coach;
    }

    public function setCoach(?Coach $coach): self
    {
        $this->coach = $coach;
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Client::class, mappedBy: 'programme')]
    private Collection $clients;

    /**
     * @return Collection<int, Client>
     */
    public function getClients(): Collection
    {
        if (!$this->clients instanceof Collection) {
            $this->clients = new ArrayCollection();
        }
        return $this->clients;
    }

    public function addClient(Client $client): self
    {
        if (!$this->getClients()->contains($client)) {
            $this->getClients()->add($client);
        }
        return $this;
    }

    public function removeClient(Client $client): self
    {
        $this->getClients()->removeElement($client);
        return $this;
    }

    #[ORM\OneToMany(targetEntity: Seance::class, mappedBy: 'programme')]
    private Collection $seances;

    public function __construct()
    {
        $this->clients = new ArrayCollection();
        $this->seances = new ArrayCollection();
    }

    /**
     * @return Collection<int, Seance>
     */
    public function getSeances(): Collection
    {
        if (!$this->seances instanceof Collection) {
            $this->seances = new ArrayCollection();
        }
        return $this->seances;
    }

    public function addSeance(Seance $seance): self
    {
        if (!$this->getSeances()->contains($seance)) {
            $this->getSeances()->add($seance);
        }
        return $this;
    }

    public function removeSeance(Seance $seance): self
    {
        $this->getSeances()->removeElement($seance);
        return $this;
    }

}
