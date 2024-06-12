import Header from '../components/Header';
import ToolCards from '../components/ToolCards';
import Layout from '../components/Layout';


export default function Home() {
  return (
    <div>
        <Layout>
      <Header isRestricted={false}/>
      <ToolCards showFooter={false} />
      </Layout>
    </div>
  );
}
