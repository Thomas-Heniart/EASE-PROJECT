import React from 'react';
import {Segment, Button, Icon} from 'semantic-ui-react';

class ChoosePasswordManager extends React.Component {
  render() {
    const {
      next,
      choosePasswordManager,
      passwordManager
    } = this.props;
    return (
      <React.Fragment>
        <p className='title center'>Where is your importation coming from</p>
        <Segment.Group>
          <Segment onClick={e => choosePasswordManager(1)} className={passwordManager === 1 ? 'selected' : null}>
            <img src="/resources/other/Excel.png"/>Excel or Google sheet</Segment>
          <Segment onClick={e => choosePasswordManager(2)} className={passwordManager === 2 ? 'selected' : null}>
            <img src="/resources/other/Chrome.png"/>Chrome</Segment>
          <Segment onClick={e => choosePasswordManager(3)} className={passwordManager === 3 ? 'selected' : null}>
            <img src="/resources/other/Dashlane.png"/>Dashlane</Segment>
          <Segment onClick={e => choosePasswordManager(4)} className={passwordManager === 4 ? 'selected' : null}>
            <img src="/resources/other/Lastpass.png"/>LastPass</Segment>
          <Segment onClick={e => choosePasswordManager(5)} className={passwordManager === 5 ? 'selected' : null}>
            <img src="/resources/other/1password.png"/>1Password</Segment>
          <Segment onClick={e => choosePasswordManager(6)} className={passwordManager === 6 ? 'selected' : null}>
            <img src="/resources/other/Keepass.png"/>Keepass</Segment>
          <Segment onClick={e => choosePasswordManager(7)} className={passwordManager === 7 ? 'selected' : null}>
            <img src="/resources/other/roboform.png"/>Roboform</Segment>
          <Segment onClick={e => choosePasswordManager(8)} className={passwordManager === 8 ? 'selected' : null}>
            <img src="/resources/other/Zohovault.png"/>Zoho Vault</Segment>
          <Segment onClick={e => choosePasswordManager(9)} className={passwordManager === 9 ? 'selected' : null}>
            <img src="/resources/other/passpack.png"/>Passpack</Segment>
        </Segment.Group>
        <Button
          className={'right'}
          disabled={passwordManager === 0}
          positive
          onClick={next}>
          Next <Icon name='arrow right'/>
        </Button>
      </React.Fragment>
    )
  }
}

export default ChoosePasswordManager;