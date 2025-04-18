<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;

use App\Repository\CommandeRepository;

#[ORM\Entity(repositoryClass: CommandeRepository::class)]
#[ORM\Table(name: 'commande')]
class Commande
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

    #[ORM\ManyToOne(targetEntity: Client::class, inversedBy: 'commandes')]
    #[ORM\JoinColumn(name: 'client_id', referencedColumnName: 'id')]
    private ?Client $client = null;

    public function getClient(): ?Client
    {
        return $this->client;
    }

    public function setClient(?Client $client): self
    {
        $this->client = $client;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $etat = null;

    public function getEtat(): ?string
    {
        return $this->etat;
    }

    public function setEtat(string $etat): self
    {
        $this->etat = $etat;
        return $this;
    }

    #[ORM\Column(type: 'datetime', nullable: true)]
    private ?\DateTimeInterface $date_livraison = null;

    public function getDate_livraison(): ?\DateTimeInterface
    {
        return $this->date_livraison;
    }

    public function setDate_livraison(?\DateTimeInterface $date_livraison): self
    {
        $this->date_livraison = $date_livraison;
        return $this;
    }

    #[ORM\Column(type: 'string', nullable: false)]
    private ?string $statut_paiement = null;

    public function getStatut_paiement(): ?string
    {
        return $this->statut_paiement;
    }

    public function setStatut_paiement(string $statut_paiement): self
    {
        $this->statut_paiement = $statut_paiement;
        return $this;
    }

    #[ORM\OneToMany(targetEntity: CommandeEquipement::class, mappedBy: 'commande')]
    private Collection $commandeEquipements;

    public function __construct()
    {
        $this->commandeEquipements = new ArrayCollection();
    }

    /**
     * @return Collection<int, CommandeEquipement>
     */
    public function getCommandeEquipements(): Collection
    {
        if (!$this->commandeEquipements instanceof Collection) {
            $this->commandeEquipements = new ArrayCollection();
        }
        return $this->commandeEquipements;
    }

    public function addCommandeEquipement(CommandeEquipement $commandeEquipement): self
    {
        if (!$this->getCommandeEquipements()->contains($commandeEquipement)) {
            $this->getCommandeEquipements()->add($commandeEquipement);
        }
        return $this;
    }

    public function removeCommandeEquipement(CommandeEquipement $commandeEquipement): self
    {
        $this->getCommandeEquipements()->removeElement($commandeEquipement);
        return $this;
    }

    public function getDateLivraison(): ?\DateTimeInterface
    {
        return $this->date_livraison;
    }

    public function setDateLivraison(?\DateTimeInterface $date_livraison): self
    {
        $this->date_livraison = $date_livraison;

        return $this;
    }

    public function getStatutPaiement(): ?string
    {
        return $this->statut_paiement;
    }

    public function setStatutPaiement(string $statut_paiement): self
    {
        $this->statut_paiement = $statut_paiement;

        return $this;
    }

}
