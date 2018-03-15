import React, {Component} from "react";
import AddAnyApp from './AddAnyApp';
import UpdatesContainer from './Updates/UpdatesContainer';
import { Grid, Image, Icon, Container } from 'semantic-ui-react';

const AppsContainer  = ({match, websites, updates, title, openModal}) => {
  return (
      <Container fluid>
        {(match.path === '/main/catalog/website' && updates.length > 0) &&
        <UpdatesContainer title={'Updates detected'} websites={websites}/>}
        <h3>
          {title}
        </h3>
        <Grid columns={4} className="logoCatalog">
          {websites.map((item) =>
            <Grid.Column key={item.id} as='a' className="showSegment" onClick={e => openModal(item)}>
              <Image src={item.logo}/>
              <p>{item.name}</p>
              <Icon name="add square"/>
            </Grid.Column>
          )}
        </Grid>
        <AddAnyApp desactivateCross query={""} focus={false}/>
      </Container>
  )
};

export default AppsContainer;