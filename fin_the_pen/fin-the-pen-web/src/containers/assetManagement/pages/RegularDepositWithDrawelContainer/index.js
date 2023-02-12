import DetailCard from './regular/DetailCard';
import Title from './regular/Title';

function RegularDepositWithdrawal() {
  return (
    <>
      <Title
        type="+"
        title="정기 입금 내역"
      />

      <DetailCard />

      <Title
        type="-"
        title="정기 출금 내역"
      />

      <DetailCard />
    </>
  );
}

export default RegularDepositWithdrawal;
